#include "Rule.h"

#include <sstream>

using namespace std;

string Rule::toString(){
  stringstream ss;
  ss << headPredicate.toString() << " :- ";
  for(unsigned int i = 0; i < predicates.size(); i++){
    ss << predicates[i].toString();
    if(i < predicates.size()-1){
      ss << ",";
    }
  }
  ss << ".";
  return ss.str();
}
