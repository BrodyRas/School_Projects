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

    queries = youngKing.giveQueries();
    r = youngKing.giveRules();

    ruling = true;
    evaluateRules();
    ruling = false;

    cout << "Query Evaluation" << endl;
    for(mi = 0; mi < queries.size(); mi++){
      //cout << "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
      //mapRelations[queries[mi].getID()].rPrint();
      evaluateQueries(queries[mi]);
    }
  };

  void createRelations();
  void populateRelations();
  Relation evaluateQueries(Predicate q);
  void evaluateRules();

  vector<int> getIndeces(vector<Parameter> ruleParameters, Tuple relsScheme);
  vector<int> getIndecesQueries(vector<Parameter> params);
  int databaseSize();

  void chooseSelect(Predicate q, Relation &tempHold);

private:
  datalogProgram youngKing;
  map<string,Relation> mapRelations;
  vector<Predicate> queries;
  vector<Rule> r;
  unsigned int mi;

  vector<int> index;

  bool adding, ruling;
};

#endif /* Database_h */
