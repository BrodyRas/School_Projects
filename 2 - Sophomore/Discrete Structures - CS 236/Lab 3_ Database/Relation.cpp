#include "Relation.h"

//The nuFacts set allows us to iterate through the set, picking up only the relevant tuples
void Relation::select(int i, string p){
  //cout << "select()" << endl;
  set<Tuple> nuFacts;
  for (set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
    if((*it)[i] == p){
      nuFacts.insert((*it)); //remove any tuple that doesn't have the proper string at the proper place
    }
  }
  facts = nuFacts;
}

void Relation::selectar(int i, int j){
  //cout << "selectar()" << endl;
  set<Tuple> nuFacts;
  for (set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
    if((*it)[i] == (*it)[j]){
      nuFacts.insert((*it)); //remove any tuple that doesn't have a double where it should
    }
  }
  facts = nuFacts;
}
///////////////////////////////////////////////////////////////////////////////////////////

void Relation::project(vector<int> index){
  //cout << "project()" << endl;
  Tuple newScheme;

  //create a new scheme
  for(unsigned int i=0; i<index.size(); i++){
    newScheme.push_back(scheme[i]);
  }

  set<Tuple> theFacts;

  //cherrypick the values from our facts, using the previously found indeces
  for (set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
    Tuple newFact = {};
    for(unsigned int i=0; i<index.size(); i++){
      newFact.push_back((*it).at(index[i]));
    }
    theFacts.insert(newFact);
  }

  //reassign the scheme and facts
  scheme = newScheme;
  facts = theFacts;

}

void Relation::rename(Tuple goodParams){
  //cout << "rename()" << endl
  scheme = goodParams;
}

string Relation::getName(){
  return name;
}

void Relation::convert(){
  while(!raw.empty()){
    string dude = raw[0].toString();
    scheme.push_back(dude);
    raw.erase(raw.begin());
  }
}



void Relation::addFact(Predicate fact){
  vector<Parameter> boi = fact.giveParameters();
  Tuple temp;
  for(unsigned int i = 0; i < boi.size(); i++){
    temp.push_back(boi[i].toString());
  }
  facts.insert(temp);
}

void Relation::addFactTuple(Tuple fact){
  facts.insert(fact);
}

set<Tuple> Relation::giveFacts(){
  return facts;
}

int Relation::factsSize(){
  return facts.size();
}


void Relation::print(Predicate q){

  cout << q.toString() << "? ";

  if(facts.size() > 0){
    cout << "Yes(" << facts.size() << ")" << endl;
    for (std::set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
      Tuple t = (*it);
      if(scheme.size() > 0){cout << "  ";}//prevents pure fact checks from adding whitespace
      for (unsigned int i = 0; i < t.size(); i++){
          cout << scheme[i] << "=" << t.at(i);
          if(i<t.size()-1){
            cout << ", ";
          }
        }
        if(scheme.size() > 0){cout << endl;} //prevents pure fact checks from adding an endline
      }
    }
  else{cout << "No" << endl;}

}
