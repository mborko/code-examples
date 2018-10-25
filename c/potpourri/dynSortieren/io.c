/*
 Name of file	: io.c
 Author			: Burkhard Hampl <burkhard.hampl@student.tgm.ac.at>
 Version		: 20141024.1
 Description	: Das C-File wo alle I/O sachen gemacht werden wie z.B. die CLI
 */

#include "sortieren.h"

/* ausgeben des arrays des structs*/
int ausgeben(int* zahlen, int groesse) {
	fprintf(stdout, "[%i", *(zahlen + 0));
	for (int i = 1; i < groesse; i++) {
		fprintf(stdout, ", %i", *(zahlen + i));
	}
	fprintf(stdout, "]\n");
	return 0;
}

/* fuellen eines Arrays mit zufalls Zahlen */
int fuellen(int* zahlen, int groesse, int maxwert) {
	srand((unsigned) time(NULL));
	for (int i = 0; i < groesse; i++) {
		*(zahlen + i) = rand() % maxwert + 1;
	}
	return 0;
}

static int doExit() {
	printf("Bye!\n");
	return EXIT_SUCCESS;
}

// Eine rote Fehler Ausgabe (dafuer muss es in einem gutes Termian verwendet werden (also von einem UNIX system))
static void printErr(char *str) {
	printf("%s%s%s\n", "\x1b[31m", str, "\x1b[0m");
}

int menue() {
	char op[11];
	zahlenSort arraySort;
	clock_t zeit;
	int maxZahlen = -1;
	int arrayLength = -1;
	int ausgabe = -1;

	printf("IndInf05 - Dynamisches Sortieren von Burkhard Hampl 4AHITT\n(Diese Programm kann jeder Zeit mit dem Befehl q beendet werden)\nViel Spass :-)\n\n");
	do {
		do {
			printf("Bitte geben Sie den gewuenschten Sortieralgorithmus ein:\n1 ... Bubblesort\n2 ... Mergesort\n3 ... Quicksort\n");
			gets(op);
			if (strcmp(op, "q") == 0)
				return doExit();
			int richtigeEingabe = -1;
			switch (strtol(op, NULL, 10)) {
			case 1:
				arraySort.sort = bubblesort;
				richtigeEingabe = 1;
				break;
			case 2:
				arraySort.sort = mergesort;
				richtigeEingabe = 1;
				break;
			case 3:
				arraySort.sort = quicksort;
				richtigeEingabe = 1;
				break;
			}
			if (richtigeEingabe == 1)
				break;
			printErr("FEHLER: FLASCHE EINGABE!");
		} while (1);

		do {
			printf("Bitte geben Sie die Zahlen Vektor groesse an!\n(Diese Zahl muss groesser als 0 sein)\n");
			gets(op);
			if (strcmp(op, "q") == 0)
				return doExit();
			arrayLength = strtol(op, NULL, 10);
			if (arrayLength > 0)
				break;
			printErr("FEHLER: FLASCHE EINGABE!");
		} while (1);

		arraySort.size = arrayLength;

		do {
			printf("Bitte geben Sie den oberen Bereich an, bis zu den die Zahlen gehen koennen!\n(Diese Zahl muss groesser als 0 sein)\n");
			gets(op);
			if (strcmp(op, "q") == 0)
				return doExit();
			maxZahlen = strtol(op, NULL, 10);
			if (maxZahlen > 0)
				break;
			printErr("FEHLER: FLASCHE EINGABE!");
		} while (1);

		do {
			printf("Wollen Sie der sortierten und unsortierten Verktor ausgegeben wird? [y/n]\n");
			gets(op);
			if (strcmp(op, "q") == 0) {
				return doExit();
			} else if (strcmp(op, "y") == 0) {
				ausgabe = 1;
				break;
			} else if (strcmp(op, "n") == 0) {
				ausgabe = 0;
				break;
			}
			printErr("FEHLER: FLASCHE EINGABE!");
		} while (1);
		// Das eigentliche Sortieren
		int array[arraySort.size];
		arraySort.array = array;
		fuellen(arraySort.array, arraySort.size, maxZahlen);
		if (ausgabe == 1) {
			printf("Der unsortierte Vektor:\n");
			ausgeben(arraySort.array, arraySort.size);
		}
		zeit = clock();
		(*arraySort.sort)(arraySort.array, arraySort.size);
		zeit = clock() - zeit;

		if (ausgabe == 1) {
			printf("Der sortierte Vektor:\n");
			ausgeben(arraySort.array, arraySort.size);
		}
		printf("Die benoetigte Zeit ist %f Sekunden\n", ((float) zeit) / CLOCKS_PER_SEC);

	} while (1);

	return EXIT_FAILURE;
}
