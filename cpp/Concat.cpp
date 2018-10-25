#include <iostream>
#include <string>
#include <sstream>
#include <cstdlib>
#include <cstdio>
using namespace std;

int main (int argc, char** argv)
{
  int jahr = 1970;
  int monat = 11;
  string mystring;
  string testDate;

  char numstr[20];
  sprintf(numstr, "%d %d", jahr, monat);
  testDate = "Date ";
  testDate += numstr;
  cout << testDate << endl;

  stringstream ss;
  ss << jahr << " " << monat;
  testDate = "Date " + ss.str() + "\n";
  cout << testDate;

  mystring = "This is the initial string content";
  cout << mystring << endl;
  mystring = "This is a different string content";
  cout << mystring << endl;
  //getline( cin, mystring );
  cin >> mystring;
  cout << "This is your text: " << mystring << endl;
  return EXIT_SUCCESS;
}
