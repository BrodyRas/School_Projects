from which_pyqt import PYQT_VER

if PYQT_VER == 'PYQT5':
    from PyQt5.QtCore import QLineF, QPointF, QObject
else:
    raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))

import time

# Some global color constants that might be useful
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (0, 0, 255)
PINK = (255, 105, 180)

# Global variable that controls the speed of the recursion automation, in seconds
#
PAUSE = .25


#
# This is the class you have to complete.
#
class ConvexHullSolver(QObject):

    # Class constructor
    def __init__(self):
        super().__init__()
        self.pause = False

    # Some helper methods that make calls to the GUI, allowing us to send updates
    # to be displayed.

    def showTangent(self, line, color):
        self.view.addLines(line, color)
        if self.pause:
            time.sleep(PAUSE)

    def eraseTangent(self, line):
        self.view.clearLines(line)

    def blinkTangent(self, line, color):
        self.showTangent(line, color)
        self.eraseTangent(line)

    def showHull(self, polygon, color):
        self.view.addLines(polygon, color)
        if self.pause:
            time.sleep(PAUSE)

    def eraseHull(self, polygon):
        self.view.clearLines(polygon)

    def showText(self, text):
        self.view.displayStatusText(text)

    # This is the method that gets called by the GUI and actually executes
    # the finding of the hull
    def compute_hull(self, points, pause, view):
        self.pause = pause
        self.view = view
        assert (type(points) == list and type(points[0]) == QPointF)

        t1 = time.time()
        points.sort(key=lambda x: x.x())
        t2 = time.time()

        t3 = time.time()
        hullPoints = convexHull(self, points)
        polygon = [QLineF(hullPoints[i], hullPoints[i + 1]) for i in range(len(hullPoints) - 1)]
        polygon += [QLineF(hullPoints[-1], hullPoints[0])]  # Ties the end to the beginning
        t4 = time.time()

        # when passing lines to the display, pass a list of QLineF objects.  Each QLineF
        # object can be created with two QPointF objects corresponding to the endpoints
        self.showHull(polygon, RED)
        self.showText('Time Elapsed (Convex Hull): {:3.3f} sec'.format(t4 - t3))


def convexHull(self, points):
    # BASE CASE: 2
    if len(points) == 2:
        return points

    # BASE CASE: 3
    elif len(points) == 3:
        # Maintain clockwise ordering by selecting the highest slope for the next point
        if findSlope(points[0], points[1]) > findSlope(points[0], points[2]):
            return [points[0], points[1], points[2]]
        else:
            return [points[0], points[2], points[1]]

    # RECURSION
    else:
        leftHull = convexHull(self, points[:len(points) // 2])
        rightHull = convexHull(self, points[len(points) // 2:])
        combinedHull = combineHulls(self, leftHull, rightHull)
        return combinedHull


def combineHulls(self, leftHull, rightHull):
    # get tangential points
    leftTop, rightTop = findUpperTangent(self, leftHull, rightHull)
    leftBot, rightBot = findLowerTangent(self, leftHull, rightHull)

    sumHull = []

    # from left-most point to the top of left
    sumHull += leftHull[:leftTop + 1]

    # from the top of right to the bottom of right
    ri = rightTop
    while ri != rightBot:
        sumHull += [rightHull[ri]]
        ri = loop(ri + 1, rightHull)
    sumHull += [rightHull[rightBot]]

    # from the bottom of left to the left-most point
    li = leftBot
    while li != 0:
        sumHull += [leftHull[li]]
        li = loop(li + 1, leftHull)

    return sumHull


def findUpperTangent(self, leftHull, rightHull):
    # right-most point of left hull, left-most point of right hull
    leftPoint = leftHull.index(sorted(leftHull, key=lambda x: x.x())[-1])
    rightPoint = rightHull.index(sorted(rightHull, key=lambda x: x.x())[0])

    done = False
    while not done:
        # Assume we've found the tangent
        done = True

        # Find initial slope
        slope = findSlope(leftHull[leftPoint], rightHull[rightPoint])

        # Iterate the right points until the highest slope is found
        while slope < findSlope(leftHull[leftPoint], rightHull[loop(rightPoint + 1, rightHull)]):
            done = False
            rightPoint = loop(rightPoint + 1, rightHull)
            slope = findSlope(leftHull[leftPoint], rightHull[rightPoint])

        # Iterate the left points until the lowest slope is found
        while slope > findSlope(leftHull[loop(leftPoint - 1, leftHull)], rightHull[rightPoint]):
            done = False
            leftPoint = loop(leftPoint - 1, leftHull)
            slope = findSlope(leftHull[leftPoint], rightHull[rightPoint])

    return leftPoint, rightPoint


def findLowerTangent(self, leftHull, rightHull):
    # right-most point of left hull, left-most point of right hull
    leftPoint = leftHull.index(sorted(leftHull, key=lambda x: x.x())[-1])
    rightPoint = rightHull.index(sorted(rightHull, key=lambda x: x.x())[0])

    done = False
    while not done:
        # Assume we've found the tangent
        done = True

        # Find initial slope
        slope = findSlope(leftHull[leftPoint], rightHull[rightPoint])

        # Iterate the right points until the lowest slope is found
        while slope > findSlope(leftHull[leftPoint], rightHull[loop(rightPoint - 1, rightHull)]):
            done = False
            rightPoint = loop(rightPoint - 1, rightHull)
            slope = findSlope(leftHull[leftPoint], rightHull[rightPoint])

        # Iterate the left points until the highest slope is found
        while slope < findSlope(leftHull[loop(leftPoint + 1, leftHull)], rightHull[rightPoint]):
            done = False
            leftPoint = loop(leftPoint + 1, leftHull)
            slope = findSlope(leftHull[leftPoint], rightHull[rightPoint])

    return leftPoint, rightPoint


# Utility function for finding slope
def findSlope(l, r):
    return (r.y() - l.y()) / (r.x() - l.x())


# this is the core of my "circular list" implementation. Modding the potential index with the size of the
# list allows for circular behavior. On the last item of a 3-item list (index 2), if you increment, you'll
# get 3 % 3 = 0, which takes us from the last item back to the first item.
def loop(candidate, hull):
    i = candidate % len(hull)
    return i
