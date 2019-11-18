/**
 * CSC 225, Assignment 7
 * Sullivan Xiong (suxiong@calpoly.edu)
 */

#include <stdio.h>

/**
 * Prints out positive integers, counting backwards from n to 1.
 * n - A positive integer at which to start
 */
void countBackwardsFrom(int n) {
    if (n == 1)
        printf("%d", n);
    else {
        printf("%d, ", n);
        countBackwardsFrom(n-1);
    }
}

/**
 * Prints out positive integers, counting forwards from 1 to n.
 * n - A positive integer at which to stop
 */
void countForwardsTo(int n) {
    if (n != 1) {
        countForwardsTo(n - 1);
        printf(", %d", n);
    }
    else 
        printf("%d", n);
}
