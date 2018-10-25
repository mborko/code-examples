#include <stdlib.h>
#include "solve9x9.h"
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
static uint16_t puzzle[81]; // static because the recursive solve function has to access it

void init_global(sudoku* s);
sudoku* convert_global();

/**
 * A recursive solving algorithm for a normal Sudoku puzzle.
 * \param i the current cell number
 * \return the next cell number
 */
static uint8_t solve(uint8_t i) {
        // Solved it!
        if (!(i ^ 81))
                return 1;

        // Skip the positions which are defined from the start.
        if (puzzle[i]) 
                return solve(i + 1);
				
				/* check column */
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
				
				/* check row */
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
				
				/* if numbers fits, modify the solution an call itself for the next cell */
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
 * Solves a regual 9x9 Sudoku puzzle.
 * \param s the sudoku to solve
 * \return the solved Sudoku or NULL if it could not be solved
 */
sudoku* solve9x9(sudoku* s) {
	init_global(s);
	if(solve(0))
		return convert_global();
	
	return NULL;
}

/**
 * Initializes the global puzzle[] with the given Sudoku to solve it afterwards.
 * \param s the sudoku
 */
void init_global(sudoku* s) {
	int i;
	for(i = 0;i < 81;i++) {
		if((*s).grid[i/9][i%9] == 0)
			puzzle[i] = 0;
		else
			puzzle[i] = 1 << ((*s).grid[i/9][i%9] - 1); // shift bits to the right, so that the number 2 = 00000010, 3 = 000000100, ..
	}
}

/**
	* Converts the global (solved) Sudoku puzzle back to a Sudoku.
	* \return the Sudoku saved in the global puzzle[]
	*/
sudoku* convert_global() {
	int i;
	sudoku* s = (sudoku*) malloc(sizeof(sudoku));

	for(i = 0;i < 81;i++) {
		(*s).grid[i/9][i%9] = 32 - __builtin_clz((unsigned int)puzzle[i]); // this function counts the leading 0s
	}

	return s;
}


