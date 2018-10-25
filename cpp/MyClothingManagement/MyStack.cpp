#include "MyStack.h"

using namespace std;

MyStack::MyStack(void)
{
}

MyStack::~MyStack(void)
{
	for(;lastindex>0;lastindex--)
		minus();
}

int MyStack::lastindex=-1;

void MyStack::plus(string c)
{
	if (c=="shirt"){
		Clothing* temp = new Shirt();
		elements.push_back(temp);
	}
	if (c=="jeans")
		elements.push_back(new Jeans());
	if (c=="dress")
		elements.push_back(new Dress());

	lastindex++;
	cout << elements.at(lastindex)->id << endl;
}

void MyStack::minus() {
	if(elements.size() > 0){
		cout << "first" << endl;
		if( lastindex > -1 ) {
			cout << elements.at(lastindex)->id << endl;
			delete elements.back();
			elements.pop_back();
			lastindex--;
		}
	}else
		cout << "Liste ist leer." << endl;
}
