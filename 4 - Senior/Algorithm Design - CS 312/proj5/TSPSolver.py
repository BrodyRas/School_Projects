#!/usr/bin/python3
import numpy

from which_pyqt import PYQT_VER
import copy

if PYQT_VER == 'PYQT5':
    from PyQt5.QtCore import QLineF, QPointF
else:
    raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))

from TSPClasses import *
import heapq


class TSPSolver:
    def __init__(self, gui_view):
        self._scenario = None

    def setupWithScenario(self, scenario):
        self._scenario = scenario

    def defaultRandomTour(self, time_allowance=60.0):
        results = {}
        cities = self._scenario.getCities()
        ncities = len(cities)
        foundTour = False
        count = 0
        bssf = None
        start_time = time.time()
        while not foundTour and time.time() - start_time < time_allowance:
            # create a random permutation
            perm = np.random.permutation(ncities)
            route = []
            # Now build the route using the random permutation
            for i in range(ncities):
                route.append(cities[perm[i]])
            bssf = TSPSolution(route)
            count += 1
            if bssf.cost < np.inf:
                # Found a valid route
                foundTour = True
        end_time = time.time()
        results['cost'] = bssf.cost if foundTour else math.inf
        results['time'] = end_time - start_time
        results['count'] = count
        results['soln'] = bssf
        results['max'] = None
        results['total'] = None
        results['pruned'] = None
        return results

    def greedy(self, time_allowance=60.0):
        results = {}
        cities = self._scenario.getCities()
        ncities = len(cities)
        count = 0
        bssf = None

        # start in the first city
        currCity = cities[0]
        visited = {0}
        route = [currCity]
        start_time = time.time()
        # visit all cities greedily
        while len(route) < ncities and time.time() - start_time < time_allowance:
            minCity = None
            minCost = float('inf')
            # find the index of the closest unvisited city
            for c in cities:
                newCost = currCity.costTo(c)
                if newCost < minCost and not visited.__contains__(c._index):
                    minCity = c
                    minCost = newCost
            # we have visited this city, no one else can
            visited.add(minCity._index)
            # append that city to the route
            route.append(minCity)
            # make that city our current city ("travel" there)
            currCity = minCity

        foundAllCities = len(route) == ncities
        if foundAllCities:
            bssf = TSPSolution(route)
            count += 1
        end_time = time.time()

        # Return the dictionary
        results['cost'] = bssf.cost if foundAllCities else math.inf
        results['time'] = end_time - start_time
        results['count'] = count
        results['soln'] = bssf
        results['max'] = None
        results['total'] = None
        results['pruned'] = None
        return results

    def branchAndBound(self, time_allowance=60.0):
        results = {}
        cities = self._scenario.getCities()
        size = len(cities)
        # using greedy, we grab a suboptimal solution to help with some initial pruning
        greedy = self.greedy(time_allowance)
        bssf = greedy['cost']
        numSolutions = 0  # how many solutions have we found so far
        QmaxSize = 0  # largest size of the queue
        numNodes = 1  # number of nodes created
        numPruned = 0  # number of pruned states

        # put the first city on the queue
        Q = []
        firstNode = QueueNode(cities[0], CostMatrix(cities), [cities[0]])
        heapq.heappush(Q, firstNode)
        route = [cities[0]]

        start_time = time.time()
        # Keep expanding children until the queue is empty, or time runs out
        while len(Q) != 0 and time.time() - start_time < time_allowance:
            QmaxSize = max(QmaxSize, len(Q))  # check maximum queue size
            curr = heapq.heappop(Q)  # Pop the front of the queue
            # only inspect if the state can give us a better cost
            if curr.matrix.bound < bssf:
                # expand the current state's children
                children = []
                for i in range(size):
                    # if the current node can reach the specified child...
                    if curr.matrix.matrix[curr.index][i] < float('inf'):
                        # make a matrix for the child state
                        newMatrix = copy.deepcopy(curr.matrix)
                        newMatrix.updatePath(curr.index, i)
                        # make a path for the child state
                        newPath = copy.copy(curr.path)
                        newPath.append(cities[i])
                        # add the new state to parent's children
                        children.append(QueueNode(cities[i], newMatrix, newPath))

                numNodes += len(children)  # record the creation of new nodes

                # Inspect each child
                for c in children:
                    # if we're at the bottom, see if this route is cheaper than our bssf
                    bottom = len(c.path) == size
                    cheaper = c.matrix.bound <= bssf
                    # have we found a new solution?
                    if bottom and cheaper:
                        bssf = c.matrix.bound
                        route = c.path
                        numSolutions += 1
                    # will this node's children potentially provide a cheaper route?
                    elif cheaper:
                        heapq.heappush(Q, c)
                    else:  # otherwise, prune the node
                        numPruned += 1
            else:  # otherwise, prune the node
                numPruned += 1
        end_time = time.time()

        # Optimal solution found
        if time.time() - start_time < time_allowance:
            bssf = TSPSolution(route)
        # Time ran out, use greedy solution
        else:
            soln = greedy['soln']
            bssf = TSPSolution(soln.route)

        # Return the dictionary
        results['cost'] = bssf.cost
        results['time'] = end_time - start_time
        results['count'] = numSolutions
        results['soln'] = bssf
        results['max'] = QmaxSize
        results['total'] = numNodes
        results['pruned'] = numPruned
        return results

    # Genetic Algorithm LETS GO!!!!!!
    def fancy(self, time_allowance=60.0):
        results = {}
        # cities = self._scenario.getCities()
        # size = len(cities)
        # cityIndices = [cities[i]._index for i in range(size)]

        start_time = time.time()

        # Generate Population
        initPopSize = 120
        population = self.initialPopulation(initPopSize)

        # Simulate Generations
        generationCount = 100
        i = 0
        while i < generationCount:
            print(f'generation: {i}')
            # Fitness: put the shortest routes up front
            population.sort(reverse=True)
            # Selection: remove the worst routes
            print(f'popSize pre-cull: {len(population)}')
            population = self.cullPopulation(population)
            print(f'popSize post-cull: {len(population)}')

            # Crossover: breed remaining individuals together
            parents = self.generatePairs(population)
            children = self.crossover(parents)
            # Mutation: potentially randomly change genes in the children
            mutChildren = self.generateMutations(children)
            # Add the children to the existing population
            print(f'mutChildren = {mutChildren}')
            population.extend(mutChildren)
            print(f'popSize post-birth: {len(population)}')
            i += 1

        # Pick our cheapest route
        population.sort(reverse=True)
        cheapest = population[0]
        end_time = time.time()

        # Get the actual solution
        soln = TSPSolution(self.indexListToCityList(cheapest.route))

        # Return the dictionary
        results['cost'] = soln.cost
        results['time'] = end_time - start_time
        results['count'] = None
        results['soln'] = soln
        results['max'] = None
        results['total'] = None
        results['pruned'] = None
        return results

    # Creates an initial population of random routes
    # returns List[List[int]]
    def initialPopulation(self, popSize):
        population = []

        # TODO: make every route using set!
        cities = self._scenario.getCities()
        cityIndices = [i for i in range(len(cities))]  # get the indices for every city
        while len(population) < popSize:
            route = random.sample(cityIndices, len(cities))
            distance = TSPSolution(self.indexListToCityList(route)).cost
            population.append(Route(route, distance))

        return list(population)

    def cullPopulation(self, population):
        lowerThird = len(population) // 3
        if (len(population) - lowerThird) % 2 != 0:
            lowerThird -= 1
        return population[:len(population) - lowerThird]

    # Randomly pair together members of population
    def generatePairs(self, population):
        parents = []

        # Pop a random element from the list
        def pop_random(lst):
            idx = random.randrange(0, len(lst))
            return lst.pop(idx)

        pop = copy.copy(population)
        while len(pop) != 0:
            r1 = pop_random(pop)
            r2 = pop_random(pop)
            pair = r1, r2  # (parent1, parent)
            parents.append(pair)

        return parents

    # Given pairs of parents, return a list of crossed children
    def crossover(self, parentPairs):
        children = []

        for p in parentPairs:
            parent1 = p[0].route
            parent2 = p[1].route

            # Get two ordered indices to select from parent 1
            a = int(random.random() * len(parent1))
            b = int(random.random() * len(parent2))
            start = min(a, b)
            end = max(a, b)
            genes1 = parent1[start:end + 1]

            genes2 = [item for item in parent2 if item not in genes1]

            route = genes2[:start] + genes1 + genes2[start:]
            distance = TSPSolution(self.indexListToCityList(route)).cost
            child = Route(route, distance)
            children.append(child)

        return children

    # For every child, run mutations
    def generateMutations(self, children):
        mutChildren = []

        # potentially mutate all the children
        for i in range(len(children)):
            mutChildren.append(self.mutate(children[i]))

        return mutChildren

    # For every index of the child, potentially swap it with another
    def mutate(self, child):
        # for every index in the child
        route = child.route
        rate = 5  # TODO: find a good rate
        for i in range(len(route)):
            # if we decide to randomly mutate
            if random.randrange(0, 100) <= rate:
                # find another index
                j = int(random.random() * len(route))
                # swap positions
                route[i], route[j] = route[j], route[i]

        return Route(route, TSPSolution(self.indexListToCityList(route)).cost)

    # During all our generation, we'll just be passing around indices, but the TSPSolver needs a list of City objects
    def indexListToCityList(self, route):
        cities = self._scenario.getCities()
        return [cities[i] for i in route]


class Route:
    def __init__(self, route, distance):
        self.route = route
        self.fitness = 1 / float(distance)  # cast as float to prevent int division # TODO: handle division by 0 ??

    def __lt__(self, other):
        assert (type(other) == Route)
        return self.fitness < other.fitness

    def __str__(self) -> str:
        return f'{self.route} : {self.fitness}'


# The Cost Matrix hold a matrix of all cities, a matrix of valid paths between them, and a bound
class CostMatrix:
    def __init__(self, cities):
        self.bound = 0

        size = len(cities)
        # initialize the matrix with distances between the cities
        self.matrix = numpy.zeros([size, size])
        for i in range(size):
            for j in range(size):
                self.matrix[i, j] = cities[i].costTo(cities[j])
        self.reduce()

    # Reduces the matrix
    def reduce(self):
        size = len(self.matrix[0])
        # ensure there's a zero in every row, unless they're all inf
        for i in range(size):
            minRowVal = numpy.amin(self.matrix[i], axis=0)
            if 0 < minRowVal < float('inf'):
                self.matrix[i] -= numpy.array([minRowVal for _ in range(size)])
                # add the subtracted elements to the bound
                self.bound += minRowVal

        # ensure there's a zero in every column, unless they're all inf
        for i in range(size):
            col = self.matrix[:, i]
            minColVal = numpy.amin(col, axis=0)
            if 0 < minColVal < float('inf'):
                col -= numpy.array([minColVal for _ in range(size)])
                # add the subtracted elements to the bound
                self.bound += minColVal

    def updatePath(self, src, dest):
        size = len(self.matrix[0])
        # the distance from the source to the destination
        dist = self.matrix[src, dest]
        # No more out-bound paths for our source
        self.matrix[src] = numpy.array([float('inf') for _ in range(size)])
        # No more in-bound paths for our destination
        self.matrix[:, dest] = numpy.array([float('inf') for _ in range(size)])
        # The destination cannot travel back to source
        self.matrix[dest, src] = float('inf')
        # Add distance to the bound, and reduce
        self.bound += dist
        self.reduce()


# These are what sit on the Priority Queue
class QueueNode:
    def __init__(self, city, matrix, path):
        self.index = city._index
        self.matrix = matrix
        self.path = path

    # How the nodes are put on the queue
    def __lt__(self, other):
        assert (type(other) == QueueNode)
        # This provides some BFS in the beginning
        if len(self.path) < 3:
            return True
        # Once the paths are sufficiently long, ditch BFS, go DFS
        return self.matrix.bound < other.matrix.bound
