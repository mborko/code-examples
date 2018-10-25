/*
 Name of file	: bubblesort.c
 Author			: Burkhard Hampl <burkhard.hampl@student.tgm.ac.at>
 Version		: 20141024.1
 Description	: Ein in C implementierter Bubblesort
 */

#include "sortieren.h"

/* Quelle: Algorithmen und Datenstrukturen in C/ Quicksort – Wikibooks, Sammlung freier Lehr-, Sach- und Fachbücher */
/* https://de.wikibooks.org/wiki/Algorithmen_und_Datenstrukturen_in_C/_Quicksort */
/* zuletzt abgerufen am 24.10.2014 */
void bubblesort(int *liste, int groesse) {
	int i, j;
	for (i = 0; i < groesse - 1; ++i) {

		for (j = 0; j < groesse - i - 1; ++j) {
			if (liste[j] > liste[j + 1]) {
				int tmp = liste[j];
				liste[j] = liste[j + 1];
				liste[j + 1] = tmp;
			}
		}
	}
}
