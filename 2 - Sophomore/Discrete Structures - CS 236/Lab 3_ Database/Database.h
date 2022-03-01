#ifndef Database_h
#define Database_h

#include "datalogProgram.h"
#include "Relation.h"
#include <iostream>
#include <sstream>
#include <string>
#include <map>

using namespace std;

class Database{
public:
  Database(datalogProgram passed){
    youngKing = passed;
    createRelations();
    populateRelations();

    q = youngKing.giveQueries();
    evaluateQueries();
  };

  void createRelations();
  void populateRelations();
  void evaluateQueries();

  void chooseSelect();

private:
  datalogProgram youngKing;
  map<string,Relation> mapRelations;
  Relation tempHold, tempSend;
  vector<Predicate> q;
  unsigned int mi;

  vector<int> index;
};

#endif /* Database_h */
