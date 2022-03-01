#include "Predicate.h"
#include <sstream>

using namespace std;

string Predicate::toString(){
  stringstream ss;
  ss << ID << "(";
  for(unsigned int i = 0; i < parameters.size(); i++){
    ss << parameters[i].toString();
    if(i < parameters.size()-1){
      ss << ",";
    }
  }
  ss << ")";
  return ss.str();
}

vector<Parameter> Predicate::giveParameters(){
  return parameters;
}
