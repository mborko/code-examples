#include <string>

using namespace std;

class Auto {
  public:
    Auto();
    ~Auto();
    void zeige();
    void bremsen(int);
  private:
    int raeder;
    int bremse;
    string farbe;
};
