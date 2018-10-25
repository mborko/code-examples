#include <iostream>

using namespace std;

class Baum
{

  protected:
	int hoehe;
	int breite;

  public:
    virtual void wachsen()=0;
    virtual void getBeschreibung()=0;
    void faellen()
    {
      cout << "BAAUUUUUUUUMMMM FAAAEEEELLLLLLTTTTT!!!!" << endl;
    }
};
