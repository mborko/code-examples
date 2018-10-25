/*
 Name of file	: main.c
 Author			: Burkhard Hampl <burkhard.hampl@student.tgm.ac.at>
 Version		: 20141024.1
 Description	: Das mani.c file, wo alle anderen files aufgerufen werden
 */

#include "sortieren.h"

int main(void) {
	/* Hiermit wird der Buffer der Konsole auf 0 gesetzt */
	/* (damit Eclipse die Ausgabe in der Konsole in der */
	/* richtigen Reihenfolge anzeigt, muss dies am Anfang */
	/* der main ausgefuehrt werden) */
	setvbuf(stdout, NULL, _IONBF, 0);

	return menue();
}
