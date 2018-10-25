#include "Jeans.h"

using namespace std;
Jeans::Jeans(void)
{
	wash=false;
}

Jeans::~Jeans(void)
{
	cout << "Jeans " << id << ": wash: " << wash << endl;
}
