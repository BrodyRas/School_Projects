#include "Relation.h"

//Adds any tuple that has the proper string at the proper place
void Relation::select(int i, string p){
  set<Tuple> nuFacts;
  for (set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
    if((*it)[i] == p){
      nuFacts.insert((*it));
    }
  }
  facts = nuFacts;
}

//Adds any tuple that has equal values at the proper indeces
void Relation::selectar(int i, int j){
  set<Tuple> nuFacts;
  for (set<Tuple>::iterator it=facts.begin(); it!=facts.end(); ++it){
    if((*it)[i] == (*it)[j]){
      nuFacts.insert((*it));
    }
  }
  facts = nuFacts;
}

//Chooses which columns will be displayed
Relation Relation::project(vector<int> index){
  Relation nuRel;
  Tuple newScheme;

  //Compiles a new scheme, using indeces given, and gives it to nuRel
  for(unsigned int i=0; i<index.size(); i++){
    newScheme.push_back(scheme[index[i]]);
  }
  nuRel.rename(newScheme);

  //Cherrypicks the values from our facts, using the previously found indeces
  for (Tuple t : facts){
    Tuple newFact = {};
    for(unsigned int j=0; j<index.size(); j++){
      newFact.push_back(t[index[j]]);
    }
    nuRel.addFactTuple(newFact);
  }
  return nuRel;
}

//Reassignes scheme of relation
void Relation::rename(Tuple goodParams){scheme = goodParams;}

//Oversees the combination of two relations
Relation Relation::join(Relation& comp){
  set<Tuple> compFacts = comp.getFacts();
  Tuple compScheme = comp.getScheme();
  Tuple nuScheme = combineTuples(scheme, scheme, compScheme, compScheme);

  //Gives the relation the combined scheme
  Relation last;
  last.rename(nuScheme);

  //Joins all Tuples from both relations
  for(Tuple T1 : facts){
    for(Tuple T2 : compFacts){
      Tuple S1 = scheme;
      Tuple S2 = comp.getScheme();
      if(joinable(T1, S1, T2, S2)){
        Tuple nuFact = combineTuples(T1, S1, T2, S2);
        last.addFactTuple(nuFact);
      }
    }
  }
  return last;
}

//Evaluates schemes and tuples to assure that a join can be performed
bool Relation::joinable(Tuple& T1, Tuple& S1, Tuple& T2, Tuple& S2){
  for(unsigned int i = 0; i < S1.size(); i++){
    for(unsigned int j = 0; j < S2.size(); j++){
      if(S1[i] == S2[j]){
        if(T1[i] != T2[j]){
          return false;
        }
      }
    }
  }
  return true;
}

//Appends one tuple to the end of another
Tuple Relation::combineTuples(Tuple& T1, Tuple& S1, Tuple& T2, Tuple& S2){
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

//Funnels facts from two relations into one relation
Relation Relation::laUnion(Relation& r){
  set<Tuple> newbies = r.getFacts();
  while(!newbies.empty()){
    Tuple boi = *newbies.begin();
    if(this->addFactTuple(*newbies.begin())){
      boi.rPrint(scheme);
    }
    newbies.erase(newbies.begin());
  }
  return *this;
}

//Creates scheme from parameters
void Relation::convert(){
  while(!raw.empty()){
    string dude = raw[0].toString();
    scheme.push_back(dude);
    raw.erase(raw.begin());
  }
}

//Adds data from predicate to Relation as Tuple
void Relation::addFact(Predicate fact){
  vector<Parameter> boi = fact.getParameters();
  Tuple temp;
  for(unsigned int i = 0; i < boi.size(); i++){
    temp.push_back(boi[i].toString());
  }
  facts.insert(temp);
}

bool Relation::addFactTuple(Tuple fact){return facts.insert(fact).second;}

string Relation::getName(){return name;}

set<Tuple> Relation::getFacts(){return facts;}

Tuple Relation::getScheme(){return scheme;}

int Relation::factsSize(){return facts.size();}

//Query Print
void Relation::qPrint(Predicate q){
  cout << q.toString() << "? ";

  if(facts.size() > 0){
    cout << "Yes(" << facts.size() << ")\n";
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
  else{cout << "No\n";}
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
