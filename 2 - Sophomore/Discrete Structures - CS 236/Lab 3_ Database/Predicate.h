#ifndef Predicate_h
#define Predicate_h

#include "Token.h"
#include "Parameter.h"
#include <iostream>
#include <string>
#include <vector>
#include <set>

using namespace std;

class Predicate{
public:
  Predicate(string ID, vector<Parameter> parameters) : ID(ID), parameters(parameters) {}
  string toString();
  string getID();
  vector<Parameter> giveParameters();
  string callParam(int i);

private:
  string ID;
  vector<Parameter> parameters;

};


#endif /* Predicate_h */
