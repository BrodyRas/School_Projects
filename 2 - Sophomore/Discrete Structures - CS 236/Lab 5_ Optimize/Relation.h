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
  Relation(){};
  Relation(Predicate nuevo){
    name = nuevo.getID();
    raw = nuevo.getParameters();
    convert();
  };

  void select(int i, string p);
  void selectar(int i, int j);
  Relation project(vector<int> index);
  void rename(Tuple goodParams);

  Relation join(Relation& comp);
  bool joinable(Tuple& T1, Tuple& S1, Tuple& T2, Tuple& S2);
  Tuple combineTuples(Tuple& T1, Tuple& S1, Tuple& T2, Tuple& S2);
  Relation laUnion(Relation& r);

  void convert();
  string getName();

  void addFact(Predicate fact);
  bool addFactTuple(Tuple fact);

  int factsSize();
  set<Tuple> getFacts();
  Tuple getScheme();

  void qPrint(Predicate q);
  void rPrint();

private:
  string name;
  Tuple scheme;
  set<Tuple> facts;

  vector<Parameter> raw;

};

#endif /* Relation_h */
