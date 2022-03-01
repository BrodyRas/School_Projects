#ifndef Rule_h
#define Rule_h

#include "Token.h"
#include "Predicate.h"
#include <iostream>
#include <string>
#include <vector>
#include <set>

using namespace std;

class Rule{
public:
  Rule(Predicate headPredicate, vector<Predicate> predicates) : headPredicate(headPredicate), predicates(predicates) {}
  string toString();

  Predicate giveHeadPredicate();
  vector<Predicate> givePredicates();

private:
  Predicate headPredicate;
  vector<Predicate> predicates;

};


#endif /* Rule_h */
