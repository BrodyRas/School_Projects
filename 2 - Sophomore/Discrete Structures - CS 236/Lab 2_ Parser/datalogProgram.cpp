#include "datalogProgram.h"
#include <iostream>

using namespace std;

void datalogProgram::print(){

  cout << "Schemes(" << schemes.size() << "):" << endl;
  for(unsigned int i=0; i < schemes.size(); i++){
    cout << "  " << schemes[i].toString() << endl;
  }

  cout << "Facts(" << facts.size() << "):" << endl;
  for(unsigned int i=0; i < facts.size(); i++){
    cout << "  " << facts[i].toString() << "." << endl;
  }

  cout << "Rules(" << rules.size() << "):" << endl;
  for(unsigned int i=0; i < rules.size(); i++){
    cout << "  " << rules[i].toString() << endl;
  }

  cout << "Queries(" << queries.size() << "):" << endl;
  for(unsigned int i=0; i < queries.size(); i++){
    cout << "  " << queries[i].toString() << "?" << endl;
  }
  unsigned int k = 0;
  cout << "Domain(" << domain.size() << "):" << endl;
  for(set<string>::iterator it = domain.begin(); it!=domain.end(); it++){
    cout << "  " << *it;
    if(k < domain.size()-1){
      cout << endl;
      k++;
    }
  }

}

void datalogProgram::addScheme(Predicate scheme){
  schemes.push_back(scheme);
}

void datalogProgram::addFact(Predicate fact){
  vector<Parameter> douglas = fact.giveParameters();
  for(unsigned int i = 0; i < douglas.size(); i++){
    domain.insert(douglas[i].toString());
  }

  facts.push_back(fact);
}

void datalogProgram::addQuery(Predicate query){
  queries.push_back(query);
}

void datalogProgram::addRule(Rule rule){
  rules.push_back(rule);
}
