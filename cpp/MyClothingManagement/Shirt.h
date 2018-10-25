#ifndef SHIRT_H
#define SHIRT_H
#include "Clothing.h"
#include <iostream>
#include <string>

using namespace std;

class Shirt : public Clothing
{
public:
	Shirt(void);
	~Shirt(void);
private:
	string color;
};
#endif
