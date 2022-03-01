#include "Token.h"
#include "Scanner.h"

#include <fstream>
#include <sstream>


using namespace std;

int main(int argc, char *argv[]){

  ifstream ifs;
  Scanner dude;

  //cout << "Opening " + string(argv[1]) + ".txt ..." << endl;
  ifs.open(string(argv[1]));

  //file found
  if(!ifs.fail()){
    //cout << string(argv[1]) + " found!" << endl;
    stringstream ss;
    ss << ifs.rdbuf();
    //cout << ss.str();
    //------------------------------------------------------------------
    dude.scan(ss.str());
    dude.print();
  }

  //cannot find specified file
  else{
    cout << string(argv[1]) + ".txt" << " cannot be found!" << endl;
  }

  return 0;
}
