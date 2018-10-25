#include "Clothing.h"
using namespace std;
Clothing::Clothing(void)
{
	idc++;
	id=idc;
	cout << "Clothing kontruktor" << endl;
}


Clothing::~Clothing(void)
{
	cout << "Clothing destruktor" << endl;
}


int Clothing::idc=0;
