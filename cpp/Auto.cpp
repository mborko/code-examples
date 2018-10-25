#include "Auto.h"
#include <iostream>
#include <string>

using namespace std;

Auto::Auto()
{
  cout << "Konstruktor wurde aufgerufen!" << endl;
}

Auto::~Auto()
{
  cout << "Destruktor wurde aufgerufen!" << endl;
}

void Auto::zeige()
{
  cout << "raeder: " << raeder << " bremse: " << bremse << " farbe: " << farbe << endl;
}

void Auto::bremsen(int kraft)
{
  cout << "wir bremsen einmal um " << kraft << " ab!" << endl;
}
