#ifndef Parser_h
#define Parser_h

#include "Scanner.h"
#include "Token.h"
#include "datalogProgram.h"
#include "Parameter.h"
#include "Predicate.h"
#include "Rule.h"
#include <iostream>
#include <string>
#include <vector>
#include <map>

using namespace std;

class Parser{
public:
  Parser(){}

  void getVector(vector<Token> vector);
  void match(TType type);
  void parse();

  void runDatalogProgram();

  void schemeList();
  void factList();
  void ruleList();
  void queryList();

  Predicate scheme();
  Predicate fact();
  Rule rule();
  Predicate query();

  Predicate headPredicate();
  Predicate predicate();

  void predicateList();
  void parameterList();
  void stringList();
  void IDList();

  void parameter();
  void expParameter();

  void expression();
  void operators();

private:
  datalogProgram youngKing;
  string exp_str;
  bool building;
  vector<Token> myVector;
  vector<Parameter> tempParam;
  vector<Predicate> tempPred;
  vector<Rule> tempRule;
  int i;
};


#endif /* Parser_h */
