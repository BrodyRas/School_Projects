#!/usr/bin/python3


from CS312Graph import *
import time


class NetworkRoutingSolver:
    def __init__(self):
        self.prev = []
        self.dist = []
        pass

    def initializeNetwork(self, network):
        assert (type(network) == CS312Graph)
        self.network = network

    def getShortestPath(self, destIndex):
        self.dest = destIndex
        path_edges = []
        total_length = 0
        # start at the end, work backwards until you find the source
        node = self.network.nodes[self.dest]
        while node != self.network.nodes[self.source]:
            prevNodeInd = self.prev[node.node_id]
            # if the destination's previous node is null, then it is unreachable
            if prevNodeInd is None:
                return {'cost': float('inf'), 'path': []}
            edge = None
            # find the edge connected to the previous node
            for e in self.network.nodes[prevNodeInd].neighbors:
                if e.dest.node_id == node.node_id:
                    edge = e
                    break
            # add this edge (and it's length) to the result, and step to the next node
            path_edges.append((edge.src.loc, edge.dest.loc, '{:.0f}'.format(edge.length)))
            total_length += edge.length
            node = edge.src
        return {'cost': total_length, 'path': path_edges}

    def computeShortestPaths(self, srcIndex, use_heap=False):
        self.source = srcIndex
        t1 = time.time()

        # fill the distance array with infinities, and the previous array with Nones
        self.dist = [float('inf')] * len(self.network.nodes)
        self.prev = [None] * len(self.network.nodes)
        self.dist[self.source] = 0

        # use the specified implementation for the queue
        if use_heap is False:
            Q = UnsortedArray(self.dist)
        else:
            Q = MinHeap(self.dist)

        # pass array of indices to the queue
        arr = []
        for i in range(len(self.network.nodes)):
            arr.append(i)
        Q.makeQueue(arr)

        while len(Q.Q):
            # find next unvisited node with shortest distance
            u = Q.delMin()
            node = self.network.nodes[u]
            # iterate through node's neighbors
            for edge in node.neighbors:
                v = edge.dest.node_id
                newLen = self.dist[u] + edge.length
                # update distance if a shorter path is found
                if self.dist[v] > newLen:
                    self.dist[v] = newLen
                    self.prev[v] = u
                    Q.decreaseKey(v)

        t2 = time.time()
        return t2 - t1


class UnsortedArray:
    def __init__(self, dist):
        self.Q = []
        self.dist = dist

    def makeQueue(self, arr):
        self.Q = arr

    def delMin(self):
        # iterate through the entire array, pop the smallest value
        ind = -1
        for i in self.Q:
            if ind == -1 or self.dist[i] < self.dist[ind]:
                ind = i
        self.Q.remove(ind)
        return ind

    def decreaseKey(self, v):
        pass


class MinHeap:
    def __init__(self, dist):
        self.Q = []
        self.dist = dist

    def makeQueue(self, arr):
        # organize the indexes based on their distances (put source at the front)
        self.Q = [x for _, x in sorted(zip(self.dist, arr), key=lambda pair: pair[0])]

    def delMin(self):
        # hold the root value, then replace with the bottom value
        minInd = self.Q[0]
        last = self.Q.pop(-1)
        if len(self.Q) > 0:
            self.Q[0] = last

        i = 0
        done = False
        while not done:
            done = True
            leftChildIndex = ((i + 1) * 2) - 1
            rightChildIndex = ((i + 1) * 2 + 1) - 1

            # makes sure the indices are within the length of the queue
            if rightChildIndex >= len(self.Q):
                if leftChildIndex >= len(self.Q):
                    return minInd
                else:
                    shortestIndex = leftChildIndex
            else:
                # figure out which child has the shorter distance
                indexArr = [leftChildIndex, rightChildIndex]
                costArr = [self.dist[self.Q[leftChildIndex]], self.dist[self.Q[rightChildIndex]]]
                shortestIndex = [x for _, x in sorted(zip(costArr, indexArr), key=lambda pair: pair[0])][0]

            # if the parent is larger than the child
            if self.dist[self.Q[i]] > self.dist[self.Q[shortestIndex]]:
                # swap the child with the parent, continue loop
                temp = self.Q[i]
                self.Q[i] = self.Q[shortestIndex]
                self.Q[shortestIndex] = temp
                i = shortestIndex
                done = False

        return minInd

    def decreaseKey(self, v):
        i = self.Q.index(v)

        done = False
        while not done:
            done = True
            p = ((i + 1) // 2) - 1

            # if we're not at the root, and our parent is bigger than us
            if i != 0 and self.dist[self.Q[i]] < self.dist[self.Q[p]]:
                # swap the parent with the child, continue loop
                temp = self.Q[p]
                self.Q[p] = self.Q[i]
                self.Q[i] = temp
                i = p
                done = False
