#include <iostream>
#include <string>
#include <cstdlib>
using namespace std;

int main (int argc, char** argv)
{
  string mystring;
  mystring = "This is the initial string content";
  cout << mystring << endl;
  mystring = "This is a different string content";
  cout << mystring << endl;
//  getline( cin, mystring );
  cin >> mystring;
  cout << "This is your text: " << mystring << endl;
  return EXIT_SUCCESS;
}
