#include "Token.h"
#include "Scanner.h"
#include "Parser.h"

#include <fstream>
#include <sstream>


using namespace std;

int main(int argc, char *argv[]){

  ifstream ifs;
  Scanner dude;
  Parser bro;

  //cout << "Opening " + string(argv[1]) + ".txt ..." << endl;
  ifs.open(string(argv[1]));

  //file found
  if(!ifs.fail()){
    //cout << string(argv[1]) + " found!" << endl;
    stringstream ss;
    ss << ifs.rdbuf();//collect entire file into stringstream

    dude.scan(ss.str());//scan stringstream

    //dude.print();//prints vector of tokens

    bro.getVector(dude.giveVector());//pass vector of tokens from scanner to parser

    bro.parse();

  }

  //cannot find specified file
  else{
    cout << string(argv[1]) << " cannot be found!" << endl;
  }

  return 0;
}
