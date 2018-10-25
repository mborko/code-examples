#include "Shirt.h"

Shirt::Shirt(void)
{
	color="Plain White";
}

Shirt::~Shirt(void)
{
	cout << "Shirt " << id << ": Color: " << color << endl;
}
