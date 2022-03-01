import random
import math


def prime_test(N, k):
    # This is main function, that is connected to the Test button. You don't need to touch it.
    return fermat(N, k), miller_rabin(N, k)


# Time Complexity: O(n^3), from cutting the y in half recursively, and squaring Z
# Space Complexity: O(n), needs to save z during the algorithm
def mod_exp(x, y, N):
    if y == 0:                  # base case
        return 1
    z = mod_exp(x, y // 2, N)   # half the exponent, recursive call
    if y % 2 == 0:              # Y is even
        return (z ** 2) % N
    else:                       # Y is odd
        return x * (z ** 2) % N


def fprobability(k):
    return 1 - 1 / 2 ** k


def mprobability(k):
    return 1 - 1 / 4 ** k


# Time Complexity: O(n^3), we are doing a a O(n^3) k times
# Space Complexity: O(n), just stores A
def fermat(N, k):
    for _ in range(k):                  # run k trials
        a = random.randint(1, N - 1)    # random number
        if mod_exp(a, N - 1, N) != 1:   # if a non-1 remainder is found, return composite
            return 'composite'
    return 'prime'


# Time Complexity: O(n^3 log n), doing mod_exp algorithm log n times (dividing the ex by 2)
# Space Complexity: O(n), storing 3 values that scale with the size of N
def miller_rabin(N, k):
    for _ in range(k):                  # run k trials
        a = random.randint(1, N - 1)    # random number
        ex = N - 1                      # exponent
        r = mod_exp(a, ex, N)           # initial result
        while r == 1 and ex % 2 == 0:   # while we have a remainder of 1, and even exponent
            ex /= 2                     # half the exponent
            r = mod_exp(a, ex, N)       # check again
        if r != 1 and r != N - 1:       # check for Carmichael numbers
            return 'composite'
    return 'prime'
