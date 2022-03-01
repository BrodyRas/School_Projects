#include "Relation.h"

///////////////////////////////////////////////////////////////////////////////////////////
//The nuFacts set allows us to iterate through the set, picking up only the relevant tuples
void Relation::select(int i, string p){
  //cout << "select()" << endl;
  set<Tuple> nuFacts;
  for (set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
    if((*it)[i] == p){
      nuFacts.insert((*it)); //Adds any tuple that has the proper string at the proper place
    }
  }
  facts = nuFacts;
}

void Relation::selectar(int i, int j){
  //cout << "selectar()" << endl;
  set<Tuple> nuFacts;
  for (set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
    if((*it)[i] == (*it)[j]){
      nuFacts.insert((*it)); //Adds any tuple that has the proper string at the proper place
    }
  }
  facts = nuFacts;
}
///////////////////////////////////////////////////////////////////////////////////////////


Relation Relation::project(vector<int> index){
  Relation nuRel;
  Tuple newScheme;

  //cout << "project:\n";
  //cout << "indeces size: " << index.size() << "\nscheme size: " << scheme.size() << endl;

  //create a new scheme
  for(unsigned int i=0; i<index.size(); i++){
    //cout << "Index Value is " << index[i] << endl;
    //cout << "Size = " << scheme.size() << endl;
    newScheme.push_back(scheme[index[i]]);
  }
  //reassign the scheme
  nuRel.setScheme(newScheme);
  //cherrypick the values from our facts, using the previously found indeces
  for (Tuple t : facts){
    //cout << "S: " << scheme.toString() << endl << t.toString() << endl;
    Tuple newFact = {};
    for(unsigned int j=0; j<index.size(); j++){
      newFact.push_back(t[index[j]]);
    }
    //cout << "N: " << newScheme.toString() << endl << newFact.toString() << endl;
    nuRel.addFactTuple(newFact);
  }
  //cout << "end project()^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" << endl;
  return nuRel;
}


//Reassign values in the scheme based on the given scheme
void Relation::rename(Tuple goodParams){
  //cout << "rename()" << endl
  scheme = goodParams;
}


//oversees the combination of two relations
Relation Relation::join(Relation comp){
  set<Tuple> compFacts = comp.giveFacts();
  Tuple nuScheme = combineTuples(scheme, scheme, comp.giveScheme(), comp.giveScheme());

  Relation last;
  last.setScheme(nuScheme);

  for(Tuple T1 : facts){
    for(Tuple T2 : compFacts){
      Tuple S1 = scheme;
      Tuple S2 = comp.giveScheme();
      if(joinable(T1, S1, T2, S2)){
        Tuple nuFact = combineTuples(T1, S1, T2, S2);
        last.addFactTuple(nuFact);
      }
    }
  }
  return last;
}


//evaluates schemes and tuples to assure that a join can be performed
bool Relation::joinable(Tuple T1, Tuple S1, Tuple T2, Tuple S2){
  for(unsigned int i = 0; i < S1.size(); i++){
    for(unsigned int j = 0; j < S2.size(); j++){
      if(S1[i] == S2[j]){
        if(T1[i] != T2[j]){
          //cout << T1.toString() << " || " << T2.toString() << endl;
          //cout << "not joinable\n";   //What does it mean???????????????????????????????????????
          return false;
        }
      }
    }
  }
  return true;
}


//appends one tuple to the end of another
Tuple Relation::combineTuples(Tuple T1, Tuple S1, Tuple T2, Tuple S2){
  Tuple last = T1;
  for(unsigned int i = 0; i < S2.size(); i++){
    bool good = true;
    for(unsigned int j = 0; j < S1.size(); j++){
      if(S2[i]==S1[j]){
        good = false;
      }
    }
    if(good){last.push_back(T2[i]);}
  }
  return last;
}


//funnels facts from two relations into one relation
Relation Relation::laUnion(Relation r){
  //r.rPrint();
  set<Tuple> newbies = r.giveFacts();
  while(!newbies.empty()){
    Tuple boi = *newbies.begin();
    if(this->addFactTuple(*newbies.begin())){
      boi.rPrint(scheme);
    }
    newbies.erase(newbies.begin());
  }
  return *this;
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


bool Relation::addFactTuple(Tuple fact){return facts.insert(fact).second;}

string Relation::getName(){return name;}

set<Tuple> Relation::giveFacts(){return facts;}

Tuple Relation::giveScheme(){return scheme;}

void Relation::setScheme(Tuple scheme){this->scheme = scheme;}

int Relation::factsSize(){return facts.size();}

//Query Print
void Relation::qPrint(Predicate q){
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

//Rule Print
void Relation::rPrint(){
  if(facts.size() > 0){
    for (Tuple t : facts){
      if(scheme.size() > 0){cout << "  ";}//prevents pure fact checks from adding whitespace
      for (unsigned int i = 0; i < t.size(); i++){
        cout << scheme[i] << "=" << t.at(i);
        if(i<t.size()-1){cout << ", ";}
      }
      if(scheme.size() > 0){cout << endl;} //prevents pure fact checks from adding an endline
    }
  }
}
