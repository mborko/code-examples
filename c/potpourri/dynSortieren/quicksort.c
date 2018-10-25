/*
 Name of file	: quicksort.c
 Author			: Burkhard Hampl <burkhard.hampl@student.tgm.ac.at>
 Version		: 20141024.1
 Description	: Ein in C implementierter Quicksort
 */

#include "sortieren.h"

/* Quelle: Algorithmen und Datenstrukturen in C/ Quicksort – Wikibooks, Sammlung freier Lehr-, Sach- und Fachbücher */
/* https://de.wikibooks.org/wiki/Algorithmen_und_Datenstrukturen_in_C/_Quicksort */
/* zuletzt abgerufen am 24.10.2014 */
static void swap(int *a, int *b) {
	int tmp = *a;
	*a = *b;
	*b = tmp;
}

// Die Quicksort funktion
static void doquicksort(int *begin, int *end) {
	int *ptr;
	int *split;
	if (end - begin <= 1)
		return;
	ptr = begin;
	split = begin + 1;
	while (++ptr != end) {
		if (*ptr < *begin) {
			swap(ptr, split);
			++split;
		}
	}
	swap(begin, split - 1);
	doquicksort(begin, split - 1);
	doquicksort(split, end);
}

// Eine funktion, die den Quicksort "richtig" aufruft
void quicksort(int* liste, int groesse) {
	doquicksort(liste, liste+groesse);
}
