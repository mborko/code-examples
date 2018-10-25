/*
 Name of file	: sortieren.h
 Author			: Burkhard Hampl <burkhard.hampl@student.tgm.ac.at>
 Version		: 20141024.1
 Description	: Das Header file wo alles was in diesm Projekt wichtig ist definiert ist
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <time.h>

#ifndef SORTIEREN_H_
#define SORTIEREN_H_

typedef struct {
	int size;
	int *array;
	int (*sort)(int *, int);

} zahlenSort;

/* Sorts */
void bubblesort(int *liste, int groesse);
void mergesort(int *liste, int groesse);
void quicksort(int *liste, int groesse);

/* io  */
int fuellen(int* zahlen, int groesse, int maxwert);
int ausgeben(int* zahlen, int groesse);
int menue();

#endif /* SORTIEREN_H_ */
