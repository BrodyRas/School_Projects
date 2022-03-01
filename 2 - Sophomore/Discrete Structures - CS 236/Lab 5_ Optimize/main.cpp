#include "Token.h"
#include "Scanner.h"
#include "Parser.h"
#include "Database.h"

#include <fstream>
#include <sstream>


using namespace std;

int main(int argc, char *argv[]){

  ifstream ifs;
  ifs.open(string(argv[1]));

  //file found
  if(!ifs.fail()){
    //**************************************************************************LAB 1
    stringstream ss;
    ss << ifs.rdbuf();//collect entire file into stringstream
    Scanner dude;
    dude.scan(ss.str());//gives stringstream to scanner
    //dude.print();//prints vector of tokens

    //**************************************************************************LAB 2
    Parser bro = Parser(dude.giveVector()); //gives vector of tokens to parser
    bro.parse();

    //**************************************************************************LAB 3 / LAB 4
    Database family = Database(bro.giveProgram());  //gives Datalog program to database

  }

  //cannot find file
  else{
    cout << string(argv[1]) << " cannot be found!\n";
  }

  return 0;
}
