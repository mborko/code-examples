#include <iostream>

using namespace std;

class Basis {
    public:
        static int id;
        int a;
        Basis();
        Basis(int a);
        ~Basis();
};

int Basis::id = 0;

Basis::Basis() {
    cout << "Standardkonstruktor" << endl;
    id++;
    a = id;
}

Basis::Basis(int a) {
    cout << "Spezialkonstruktor " << a << endl;
    id++;
    this->a = a;
}

Basis::~Basis() {
    cout << "Standarddestruktor " << a << endl;
}

class Kind : public Basis {
    public:
        string name;
        Kind();
        ~Kind();
};

//Kind::Kind() : Basis(0) {
Kind::Kind() {
    Basis();
    cout << "Standardkonstruktor Kind" << endl;
    name = "namenlos";
    Basis::Basis();
}

Kind::~Kind() {
    cout << "Standarddestruktor Kind " << name << endl;
}

int main()
{
    cout << "Erzeuge b1 am Heap" << endl;
    Basis* b1 = new Basis();

    cout << "Erzeuge b2 am Heap" << endl;
    Basis* b2 = new Basis(2);

    cout << "Erzeuge b3 am Stack" << endl;
    Basis b3;

    cout << "Erzeuge b4 am Stack" << endl;
    Basis b4(4);

    cout << "Erzeuge k1 am Heap" << endl;
    Kind* k1 = new Kind();

    cout << "Manuelle Destruktoren" << endl;
    delete b1;
    delete b2;
    delete k1;

    cout << "Automatische Destruktoren" << endl;
    return 0;
}
