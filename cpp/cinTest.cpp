#include <iostream>
#include <cstdlib>
#include <string>
#include <sstream>
using namespace std;

int main (int argc, char** argv)
{
	cout << "Du hast " << argc << " Argumente übergeben!" << endl;
	cout << "Programmname: " << *argv << endl;
	cout << "1.Argument: " << *(argv+1) << endl;
	string mystr;
	int d=1;
	cout << "gib mir bitte eine zahl: ";
	getline (cin, mystr);
	stringstream(mystr) >> d;
	cout << "\ndas ist der wert*2, den du mir geliefert hast: " << d*2 << endl;
	return EXIT_SUCCESS;
}
