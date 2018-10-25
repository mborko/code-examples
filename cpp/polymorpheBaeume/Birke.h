#include <iostream>
#include "Baum.h"

using namespace std;

class Birke : public Baum
{
  private:
    void superPrivateMethode(void)
    {
    	cout << "Du darfst mich nur von innen aufrufen!" << endl;
    }
    
  protected:
  	void nurFuerAbgeleiteteKlassen(void)  
    {
    	cout << "Du darfst mich von aussen nicht aufrufen!" << endl;
    }
    
  public:
    void wachsen()
    {
      hoehe+=2;
      breite++;
    }

    void getBeschreibung()
    {
      cout << "Das ist eine Birke!" << endl;
      superPrivateMethode();
    }
    
    void neueFunktion()
    {
	  cout << "Die Rinde ist weisssss" << endl;
    }
};
