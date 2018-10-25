#include "solvex.h"
#include <stdlib.h>

// LSB(x) returns the index of the LSB of the number x
#define LSB(x) ( 31 - __builtin_clz((unsigned int)(x)) ) 

/*
 * From: https://github.com/AntonFagerberg/Sudoku-C/blob/master/sudoku.c
 * Sudoku Solver (brute-force).
 * with the purpose of beeing fast as hell. It's pretty fast.
 * 2012 Anton Fagerberg [anton at antonfagerberg dot com]
 * wwww.antonfagerberg.com
 */

/* "Near worst case" Sudoku
 * http://en.wikipedia.org/wiki/Sudoku_algorithms#Brute-force_algorithm
 */

static uint16_t puzzle[81];

/**
 * Solve the Sudoku X saved in the global
 * array 'puzzle'.
 * \param i the i-th cell
 * \return 1 if the solution was found
 *      or 0 otherwise
 */
static int8_t solve(uint8_t i) {
        // Solved it!
        if (!(i ^ 81))
                return 1;

        // Skip the positions which are defined from the start.
        if (puzzle[i]){
           return solve(i + 1);
        }

        uint8_t align = i % 9;
        uint16_t valid_numbers = puzzle[ align ];
        valid_numbers = valid_numbers | puzzle[ align + 9 ];
        valid_numbers = valid_numbers | puzzle[ align + 18 ];
        valid_numbers = valid_numbers | puzzle[ align + 27 ];
        valid_numbers = valid_numbers | puzzle[ align + 36 ];
        valid_numbers = valid_numbers | puzzle[ align + 45 ];
        valid_numbers = valid_numbers | puzzle[ align + 54 ];
        valid_numbers = valid_numbers | puzzle[ align + 63 ];
        valid_numbers = valid_numbers | puzzle[ align + 72 ];

        align = i / 9 * 9;
        valid_numbers = valid_numbers | puzzle[ align ];
        valid_numbers = valid_numbers | puzzle[ align + 1 ];
        valid_numbers = valid_numbers | puzzle[ align + 2 ];
        valid_numbers = valid_numbers | puzzle[ align + 3 ];
        valid_numbers = valid_numbers | puzzle[ align + 4 ];
        valid_numbers = valid_numbers | puzzle[ align + 5 ];
        valid_numbers = valid_numbers | puzzle[ align + 6 ];
        valid_numbers = valid_numbers | puzzle[ align + 7 ];
        valid_numbers = valid_numbers | puzzle[ align + 8 ];
        
        // Check "squares"
        align = 27 * (i / 9 / 3) + 3 * (i / 3 % 3);
        valid_numbers = valid_numbers | puzzle[ align ];
        valid_numbers = valid_numbers | puzzle[ align + 9 ];
        valid_numbers = valid_numbers | puzzle[ align + 18 ];

        valid_numbers = valid_numbers | puzzle[ align + 1 ];
        valid_numbers = valid_numbers | puzzle[ align + 10 ];
        valid_numbers = valid_numbers | puzzle[ align + 19 ];
        
        valid_numbers = valid_numbers | puzzle[ align + 2 ];
        valid_numbers = valid_numbers | puzzle[ align + 11 ];
        valid_numbers = valid_numbers | puzzle[ align + 20 ];
        
        // Check Diagonals
        if(i % 10 == 0){
          valid_numbers = valid_numbers | puzzle[ 0 ];
          valid_numbers = valid_numbers | puzzle[ 10 ];
          valid_numbers = valid_numbers | puzzle[ 20 ];
          valid_numbers = valid_numbers | puzzle[ 30 ];
          valid_numbers = valid_numbers | puzzle[ 40 ];
          valid_numbers = valid_numbers | puzzle[ 50 ];
          valid_numbers = valid_numbers | puzzle[ 60 ];
          valid_numbers = valid_numbers | puzzle[ 70 ];
          valid_numbers = valid_numbers | puzzle[ 80 ];
        }
        if(i % 8 == 0){
          valid_numbers = valid_numbers | puzzle[ 8 ];
          valid_numbers = valid_numbers | puzzle[ 16 ];
          valid_numbers = valid_numbers | puzzle[ 24 ];
          valid_numbers = valid_numbers | puzzle[ 32 ];
          valid_numbers = valid_numbers | puzzle[ 40 ];
          valid_numbers = valid_numbers | puzzle[ 48 ];
          valid_numbers = valid_numbers | puzzle[ 56 ];
          valid_numbers = valid_numbers | puzzle[ 64 ];
          valid_numbers = valid_numbers | puzzle[ 72 ];
        }

        uint8_t next = i + 1;
        if (!(valid_numbers & 1)) {
                puzzle[i] = 1;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 2)) {
                puzzle[i] = 2;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 4)) {
                puzzle[i] = 4;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 8)) {
                puzzle[i] = 8;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 16)) {
                puzzle[i] = 16;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 32)) {
                puzzle[i] = 32;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 64)) {
                puzzle[i] = 64;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 128)) {
                puzzle[i] = 128;
                if (solve(next))
                        return 1;
        }
        if (!(valid_numbers & 256)) {
                puzzle[i] = 256;
                if (solve(next))
                        return 1;
        }

        return puzzle[i] = 0;
}

/**
 * Returns a solved Sudoku-X of 
 * some given Sudoku-X or NULL if
 * no solution was found.
 * \return NULL if there is no solution or
 *         the solved sudoku otherwise
 */
sudoku* solvex(sudoku* s){
  int i;
  sudoku* result = NULL;
  for(i = 0; i < 81; i++){
      if(s->grid[i/9][i%9])
        puzzle[i] = 1 << (s->grid[i/9][i%9] - 1);
      else
        puzzle[i] = 0;
  }
  if(solve(0)){
    result = malloc(sizeof(sudoku));
    for(i = 0; i < 81; i++)
      result->grid[i/9][i%9] = LSB(puzzle[i]) + 1;
  } 
  return result;
}


