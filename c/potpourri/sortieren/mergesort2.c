/* Autor: Philipp Lasser
 * Datum: 21.01.2009
 * Version: 1.1
 * Programmbeschreibung: Dies ist ein Programm das 10 eingegebenen 
 * Zahlen, aufsteigend sortiert wieder ausgiebt. 
 */
void mergesort(int a[], int low, int high) { //Funktion mergesort
  int i = 0;
  int elemente = high - low + 1; //Anzahl der Elemente
  int hilfsarray  = 0;
  int FeldL = 0;
  int FeldR = 0;
  int arbeitsarray[elemente];

  if(low == high) //wenn nichts zu sortieren ist
    return;

  hilfsarray  = (low + high) / 2;

  mergesort(a, low, hilfsarray); //Rekursion
  mergesort(a, hilfsarray + 1, high); 

  for(i = 0; i < elemente; i++)
    arbeitsarray[i] = a[low + i];  //schreiben ins Arbeitsarray

  FeldL = 0;
  FeldR = hilfsarray - low + 1;

  for(i = 0; i < elemente; i++) {

    if(FeldR <= high - low) //Vergleichen der 
      if(FeldL <= hilfsarray - low)


	if(arbeitsarray[FeldL] > arbeitsarray[FeldR]){   //Die Teilfelder wieder vereinigen
	  a[i + low] = arbeitsarray[FeldR++];
	}
	else {
	  a[i + low] = arbeitsarray[FeldL++];
	}
      else {
	a[i + low] = arbeitsarray[FeldR++];
      }
    else {		
      a[i + low] = arbeitsarray[FeldL++];
    }
  }

}
