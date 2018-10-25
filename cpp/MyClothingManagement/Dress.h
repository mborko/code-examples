#ifndef DRESS_H
#define DRESS_H
#include "Clothing.h"
#include <iostream>
#include <string>

using namespace std;

class Dress : public Clothing
{
public:
	Dress(void);
	~Dress(void);
private:
	int length;
};
#endif
