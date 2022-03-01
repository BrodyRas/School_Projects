#ifndef datalogProgram_h
#define datalogProgram_h

#include "Predicate.h"
#include "Rule.h"
#include "Token.h"
#include "Parameter.h"
#include <iostream>
#include <string>
#include <vector>
#include <set>

using namespace std;

class datalogProgram{
public:
  datalogProgram(){}
  void print();
  void addScheme(Predicate scheme);
  void addFact(Predicate fact);
  void addQuery(Predicate query);
  void addRule(Rule rule);

  vector<Predicate> giveSchemes();
  vector<Predicate> giveFacts();
  vector<Predicate> giveQueries();
  vector<Rule> giveRules();


private:
  vector<Predicate> schemes, facts, queries;
  vector<Rule> rules;
  set<string> domain;
};


#endif /* datalogProgram_h */
