#!/usr/bin/python3

from which_pyqt import PYQT_VER

if PYQT_VER == 'PYQT5':
    from PyQt5.QtCore import QLineF, QPointF
else:
    raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))

import math
import time
import random

# Used to compute the bandwidth for banded version
MAXINDELS = 3
BAND = (MAXINDELS * 2) + 1

# Used to implement Needleman-Wunsch scoring
MATCH = -3
INDEL = 5
SUB = 1


class GeneSequencing:

    def __init__(self):
        pass

    def align(self, seq1, seq2, banded, align_length):
        self.banded = banded
        self.MaxCharactersToAlign = align_length

        sequencer = Sequencer(seq1, seq2, self.banded, self.MaxCharactersToAlign)
        score, alignment1, alignment2 = sequencer.run()

        return {'align_cost': score, 'seqi_first100': alignment1, 'seqj_first100': alignment2}


class Sequencer:
    def __init__(self, seq1, seq2, banded, align):
        self.seq1 = seq1[:align]
        self.seq2 = seq2[:align]
        self.banded = banded
        self.aTable = []
        self.bTable = []
        self.maxAlign = align

    def run(self):
        if self.banded:
            return self.runBanded()
        else:
            return self.runUnbanded()

    # runs the unbanded algorithm, checking every possible cell
    def runUnbanded(self):
        # initialize our tables
        self.aTable = [[None for _ in range(len(self.seq2) + 1)] for _ in range(len(self.seq1) + 1)]
        self.bTable = [[None for _ in range(len(self.seq2) + 1)] for _ in range(len(self.seq1) + 1)]

        for i in range(len(self.seq1) + 1):
            for j in range(len(self.seq2) + 1):
                # Fills in the first row and column with INDEL values
                if i == 0:
                    self.aTable[i][j] = INDEL * j
                    self.bTable[i][j] = 'u'
                elif j == 0:
                    self.aTable[i][j] = INDEL * i
                    self.bTable[i][j] = 'l'
                else:
                    self.updateCell(i, j)

        # Construct the alignments
        a1, a2 = self.buildAlignment()

        # return the optimal scores and alignments
        return self.aTable[len(self.seq1)][len(self.seq2)], a1[:100], a2[:100]

    def updateCell(self, i, j):
        # look at our three neighbors
        up = self.aTable[i][j - 1]
        left = self.aTable[i - 1][j]
        diagonal = self.aTable[i - 1][j - 1]

        # Inserting or deleting a character costs 5 points
        # The if/else trees are for the banded algorithm
        if up is None:
            u = float('inf')
        else:
            u = up + INDEL
        if left is None:
            l = float('inf')
        else:
            l = left + INDEL

        # if the characters match, reward 3 points, otherwise substitute for 1 point
        if self.seq1[i - 1] == self.seq2[j - 1]:
            d = diagonal + MATCH
        else:
            d = diagonal + SUB

        # finds the index of the smallest cost
        lens = [u, l, d]
        ind = lens.index(min(lens))

        # records that cost in the value table and back table
        if ind == 0:
            self.aTable[i][j] = u
            self.bTable[i][j] = 'u'
        elif ind == 1:
            self.aTable[i][j] = l
            self.bTable[i][j] = 'l'
        elif ind == 2:
            self.aTable[i][j] = d
            self.bTable[i][j] = 'd'

    # builds the alignments for the two sequences
    def buildAlignment(self):
        a1, a2 = "", ""
        i = len(self.seq1)
        j = len(self.seq2)
        # start at the end, work backwards
        pointer = self.bTable[i][j]
        # until you reach [0][0]...
        self.bTable[0][0] = None
        while pointer is not None:
            # pull the appropriate character from each string, using dashes for INDELs
            if pointer == 'l':
                a1 = self.seq1[i - 1] + a1
                a2 = '-' + a2
                i -= 1
            if pointer == 'u':
                a1 = '-' + a1
                a2 = self.seq2[j - 1] + a2
                j -= 1
            if pointer == 'd':
                a1 = self.seq1[i - 1] + a1
                a2 = self.seq2[j - 1] + a2
                j -= 1
                i -= 1
            pointer = self.bTable[i][j]

        return a1, a2

    # runs the banded algorithm, only checking a band of 7 around the middle
    def runBanded(self):
        # don't run if the size discrepancy is too big
        lenDiff = abs(len(self.seq1) - len(self.seq2))
        if lenDiff > 0:
            return float('inf'), "No Alignment Possible", "No Alignment Possible"

        # initialize our tables
        self.aTable = [[None for _ in range(BAND)] for _ in range(len(self.seq1) + 1)]
        self.bTable = [[None for _ in range(BAND)] for _ in range(len(self.seq1) + 1)]

        x = 0
        for y in range(len(self.seq1) + 1):
            # Fills in the first row and column with INDEL values
            if y == 0:
                for j in range(MAXINDELS + 1):
                    self.aTable[y][j] = INDEL * j
                    self.bTable[y][j] = 'u'
                    self.aTable[j][y] = INDEL * j
                    self.bTable[j][y] = 'l'
            # Fills in the subsequent rows
            else:
                # only increment our horizontal iterator if we're not in the middle section
                if not self.inMiddle(y):
                    x += 1
                # used to draw our "downward" line
                offset = 0
                for j in range(MAXINDELS + 1):
                    # Fills the cells to the right of our pointer
                    if x + j < BAND:
                        self.updateCellBanded(y, x + j)
                    # Fills cells below (or below and to the left)
                    if self.inMiddle(y + j) and j > 0:
                        offset += 1
                        self.updateCellBanded(y + j, x - offset)
                    elif self.inEnd(y + j):
                        if y + j < len(self.seq1) + 1:
                            self.updateCellBanded(y + j, x - offset)  # offset is kept here for potentially jagged tails
                    else:
                        self.updateCellBanded(y + j, x)

        # Construct the alignments
        a1, a2 = self.buildAlignmentBanded()

        return self.aTable[len(self.seq1)][BAND - 1], a1[:100], a2[:100]

    def updateCellBanded(self, i, j):
        # look at our three neighbors
        if self.inMiddle(i):
            left = self.aTable[i][j - 1]
            if j + 1 >= (2 * MAXINDELS) + 1:
                up = float('inf')
            else:
                up = self.aTable[i - 1][j + 1]
            diagonal = self.aTable[i - 1][j]
        else:
            left = self.aTable[i][j - 1]
            up = self.aTable[i - 1][j]
            diagonal = self.aTable[i - 1][j - 1]

        # Inserting or deleting a character costs 5 points
        # The if/else trees are for the banded algorithm
        if left is None:
            l = float('inf')
        else:
            l = left + INDEL
        if up is None:
            u = float('inf')
        else:
            u = up + INDEL

        # if the characters match, reward 3 points, otherwise substitute for 1 point
        if self.seq1[i - 1] == self.seq2[j - 1]:
            d = diagonal + MATCH
        else:
            d = diagonal + SUB

        # finds the index of the smallest cost
        lens = [l, u, d]
        ind = lens.index(min(lens))

        # records that cost in the value table and back table
        if ind == 0:
            self.aTable[i][j] = l
            self.bTable[i][j] = 'u'
        elif ind == 1:
            self.aTable[i][j] = u
            self.bTable[i][j] = 'l'
        elif ind == 2:
            self.aTable[i][j] = d
            self.bTable[i][j] = 'd'

    # builds the banded alignments for the two sequences
    def buildAlignmentBanded(self):
        a1, a2 = "", ""
        i = len(self.seq1)
        j = BAND - 1
        len1 = len(self.seq1) - 1
        len2 = len(self.seq2) - 1
        # start at the end, work backwards
        pointer = self.bTable[i][j]
        # until you reach [0][0]...
        self.bTable[0][0] = None
        while pointer is not None:
            # pull the appropriate character from each string, using dashes for INDELs
            if self.inMiddle(i):
                if pointer == 'l':
                    a1 = self.seq1[len1] + a1
                    a2 = '-' + a2
                    len1 -= 1
                    j -= 1
                if pointer == 'u':
                    a1 = '-' + a1
                    a2 = self.seq2[len2] + a2
                    len2 -= 1
                    j += 1
                    i -= 1
                if pointer == 'd':
                    a1 = self.seq1[len1] + a1
                    a2 = self.seq2[len2] + a2
                    len1 -= 1
                    len2 -= 1
                    i -= 1
            else:
                if pointer == 'l':
                    a1 = self.seq1[len1] + a1
                    a2 = '-' + a2
                    len1 -= 1
                    j -= 1
                if pointer == 'u':
                    a1 = '-' + a1
                    a2 = self.seq2[len2] + a2
                    len2 -= 1
                    i -= 1
                if pointer == 'd':
                    a1 = self.seq1[len1] + a1
                    a2 = self.seq2[len2] + a2
                    len1 -= 1
                    len2 -= 1
                    j -= 1
                    i -= 1
            pointer = self.bTable[i][j]

        return a1, a2

    def inMiddle(self, i):
        return MAXINDELS < i < len(self.seq1) + 1 - MAXINDELS

    def inEnd(self, i):
        return i >= len(self.seq1) + 1 - MAXINDELS
