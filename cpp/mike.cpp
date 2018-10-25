#include <iostream>
#include <string>

using namespace std;

class Papa {
	public:
		static int oc;
		Papa();
		Papa(int);
		~Papa();
		int zahl;
	private:
		int id;
};
int Papa::oc = 0;

Papa::Papa() {
	oc++;
	id = oc;
	cout << id << ". Papa-Objekt erstellt" << endl;
}

Papa::Papa(int zahl) {
	Papa();
	this->zahl = zahl;
}

Papa::~Papa() {
	cout << id << ". Papa-Objekt wird zerstoert" << endl;
}

class Sohn : public Papa {
	public:
		static int oc;
		Sohn();	
		~Sohn();
	private:
		int id;
};
int Sohn::oc = 0;

Sohn::Sohn() {
	oc++;
	id = oc;
	Papa(1234);
	cout << id << ". Sohn-Objekt erstellt" << endl;
	cout << zahl << " => zahl" << endl;
}
Sohn::~Sohn() {
	cout << id << ". Sohn-Objekt wird zerstoert" << endl;
}

int main(int argc, char** argv) {
	Papa p1;
	Papa p2(1234);
	Sohn s1;
}
