#include <iostream>
#include <cstdlib>
#include <sstream>
using namespace std;

int main(int argc, char** argv)
{
  int temp;
  if ( argc >= 2 ) {
    if ( ! ( stringstream(argv[1]) >> temp ) ){
      cout << "That was not an integer...\nTry again!" << endl;
      return EXIT_FAILURE;
    } else {
      cout << temp << " is an integer... Yippieee!" << endl;
    }
  } else {
    cout << "Please give one integer value as argument!" << endl;
    return EXIT_FAILURE;
  }
  return EXIT_SUCCESS;
}
