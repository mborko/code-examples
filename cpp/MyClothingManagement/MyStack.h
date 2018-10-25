#ifndef MYSTACK_H
#define MYSTACK_H
#include "Clothing.h"
#include <stdio.h>
#include <iostream>
#include <vector>
#include <string.h>
#include "Jeans.h"
#include "Dress.h"
#include "Shirt.h"

using namespace std;

class MyStack
{
private:
	vector<Clothing*> elements;
	static int lastindex;
public:
	MyStack(void);
	~MyStack(void);
	void plus(string c);
	void minus();
};
#endif
