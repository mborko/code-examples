// MyStackManagement.cpp : Defines the entry point for the console application.
//
#include <cstdio>
#include <iostream>
#include <string>
#include "MyStack.h"

using namespace std;
int main()
{
	MyStack* stacki = new MyStack();
	string c;

	do{
		cout <<"Enter \"shirt\", \"jeans\", \"dress\", \"minus\", or \"abort\""<<endl;
		cin >> c;

		if (c == "shirt" || c == "jeans" || c == "dress" )
			stacki->plus(c);

		if (c == "minus")
			stacki->minus();
	}while(c != "abort");

	return 0;
}
