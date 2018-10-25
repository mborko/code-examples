#include <cstdlib>
#include "Birke.h"
#include "SchwarzBirke.h"

int main (int argc, char** argv)
{
  Birke b;
  SchwarzBirke s;

  b.wachsen();
  b.wachsen();
  b.getBeschreibung();
  b.faellen();
  b.neueFunktion();

  s.qed();

  return EXIT_SUCCESS;
}
