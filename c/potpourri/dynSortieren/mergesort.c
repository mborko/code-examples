/*
 Name of file	: mergesort.c
 Author			: Burkhard Hampl <burkhard.hampl@student.tgm.ac.at>
 Version		: 20141024.1
 Description	: Ein in C implementierter mergesort
 */

#include "sortieren.h"

/* Quelle: Algorithmen und Datenstrukturen in C/ Mergesort – Wikibooks, Sammlung freier Lehr-, Sach- und Fachbücher */
/* https://de.wikibooks.org/wiki/Algorithmen_und_Datenstrukturen_in_C/_Mergesort */
/* zuletzt abgerufen am 22.10.2014 */
void mergesort(int* liste, int groesse) {
	if (groesse > 1) {

		int haelfte1[groesse / 2];
		int haelfte2[(groesse + 1) / 2];
		int i;
		for (i = 0; i < groesse / 2; ++i)
			haelfte1[i] = liste[i];
		for (i = groesse / 2; i < groesse; ++i)
			haelfte2[i - groesse / 2] = liste[i];

		mergesort(haelfte1, groesse / 2);
		mergesort(haelfte2, (groesse + 1) / 2);

		int *pos1 = &haelfte1[0];
		int *pos2 = &haelfte2[0];
		for (i = 0; i < groesse; ++i) {
			if (*pos1 <= *pos2) {
				liste[i] = *pos1;
				if (pos1 != &haelfte2[(groesse + 1) / 2 - 1]) { // pos1 nicht verändern, wenn der größte Wert mehrmals vorkommt
					if (pos1 == &haelfte1[groesse / 2 - 1]) {
						pos1 = &haelfte2[(groesse + 1) / 2 - 1];
					} else {
						++pos1;
					}
				}
			} else {
				liste[i] = *pos2;
				if (pos2 == &haelfte2[(groesse + 1) / 2 - 1]) {
					pos2 = &haelfte1[groesse / 2 - 1];
				} else {
					++pos2;
				}
			}
		}
	}
}
