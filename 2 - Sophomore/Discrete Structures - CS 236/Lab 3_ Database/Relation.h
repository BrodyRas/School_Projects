#ifndef Relation_h
#define Relation_h

#include "Tuple.h"
#include "Predicate.h"

#include <iostream>
#include <sstream>
#include <string>
#include <map>
#include <set>

using namespace std;

class Relation{
public:
  Relation(){
    first = true;
  };
  Relation(Predicate nuevo){
    name = nuevo.getID();
    raw = nuevo.giveParameters();
    convert();
  };

  void select(int i, string p);
  void selectar(int i, int j);
  void project(vector<int> index);
  void rename(Tuple goodParams);

  void convert();
  string getName();

  void addFact(Predicate fact);
  void addFactTuple(Tuple fact);
  int factsSize();
  set<Tuple> giveFacts();

  void print(Predicate q);

private:
  string name;
  Tuple scheme;
  set<Tuple> facts;

  vector<Parameter> raw;

  bool first;
};

#endif /* Relation_h */
