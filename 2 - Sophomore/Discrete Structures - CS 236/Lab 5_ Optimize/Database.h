#ifndef Database_h
#define Database_h

#include "datalogProgram.h"
#include "Relation.h"
#include "Node.h"
#include "Graph.h"
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

    queries = youngKing.getQueries();
    r = youngKing.getRules();

    dependencyGraph();

    SCC = forwardGraph.getSCC();

    ruling = true;
    cout << "\nRule Evaluation\n";
    for(unsigned int i = 0; i < SCC.size(); i++){evaluateRules(SCC[i]);}
    ruling = false;

    cout << "\nQuery Evaluation\n";
    for(unsigned int i = 0; i < queries.size(); i++){evaluateQueries(queries[i]);}

  };

  void createRelations();
  void populateRelations();

  void dependencyGraph();
  void evaluateRules(set<int> setSCC);
  void ruleLogic(int i);
  Relation evaluateQueries(Predicate q);

  void chooseSelect(Predicate q, Relation &tempHold);
  vector<int> getIndeces(vector<Parameter> ruleParameters, Tuple relsScheme);
  vector<int> getIndecesQueries(vector<Parameter> params);
  int databaseSize();

private:
  datalogProgram youngKing;
  map<string,Relation> mapRelations;

  vector<Predicate> queries;
  vector<Rule> r;

  bool adding, ruling;

  Graph forwardGraph, reverseGraph;
  vector<set<int>> SCC;
};

#endif /* Database_h */
