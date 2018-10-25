#include "Dress.h"

using namespace std;

Dress::Dress(void)
{
	length=100;
}

Dress::~Dress(void)
{
	cout << "Dress " << id << ": Length: " << length << endl;
}
