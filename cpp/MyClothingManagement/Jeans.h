#ifndef JEANS_H
#define JEANS_H
#include "Clothing.h"
#include <iostream>

class Jeans : public Clothing
{
public:
	Jeans(void);
	~Jeans(void);
private:
	bool wash;
};
#endif
