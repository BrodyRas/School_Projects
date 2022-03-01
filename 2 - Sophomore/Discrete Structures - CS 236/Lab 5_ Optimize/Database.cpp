#include "Database.h"
#include <string>

//Creates relations based on the schemes provided in program
void Database::createRelations(){
  vector<Predicate> boi = youngKing.getSchemes();
  for(unsigned int i = 0; i < boi.size(); i++){
    Relation dude = Relation(boi[i]);
    mapRelations.insert(std::pair<string,Relation>(dude.getName(),dude));
  }
}

//Fills relations with tuples
void Database::populateRelations(){
  vector<Predicate> dude = youngKing.getFacts();
  for(unsigned int i = 0; i < dude.size(); i++){
    mapRelations[dude[i].getID()].addFact(dude[i]);
  }
}

//Discovers dependencies between rules, to avoid unnecesarry repetitions
void Database::dependencyGraph(){
  cout << "Dependency Graph\n";
  //Compares the predicates of each rule to each other rule;
  for(unsigned int i = 0; i < r.size(); i++){
    forwardGraph.addNode(i);
    reverseGraph.addNode(i);
    for(unsigned int j = 0; j < r[i].getPredicates().size(); j++){
      for(unsigned int k = 0; k < r.size(); k++){
        //If the ID of this predicate matches the ID of any rule, assign dependency
        Predicate comp = r[i].getPredicates()[j];
        if(comp.getID() == r[k].getHeadPredicate().getID()){
          forwardGraph.assignDependency(i,k);
          reverseGraph.assignDependency(k,i);
        }
      }
    }
  }
  cout << ""; //This line is cursed? It will break the program if removed
  forwardGraph.print();

  reverseGraph.DFSForest();

  stack<int> postorder = reverseGraph.getPostorder();

  forwardGraph.findSCC(postorder);
}

//Adds new tuples to the database, by combinining data of relevant relations
void Database::evaluateRules(set<int> setSCC){
  int passes = 0;

  //Creates list of rules to be evaluated
  stringstream ss; unsigned int j = 0;
  for(int i : setSCC){
    ss << "R" << i;
    if(j < setSCC.size()-1){
      ss << ",";
    }
    j++;
  }

  //if there's a single, non-reflexive rule, only evaluate once
  if(setSCC.size() == 1 && !forwardGraph.isReflexive(*setSCC.begin())){
    cout << "SCC: R" << *setSCC.begin() << endl;
    cout << r[*setSCC.begin()].toString() << endl;
    ruleLogic(*setSCC.begin());
    passes++;
    cout << passes << " passes: R" << *setSCC.begin() << endl;
  }


  //More than one rule, reflexives included
  else{
    cout << "SCC: " << ss.str() << endl;
      //Until no tuples are added, evaluate every rule in the set
      adding = true;
      do{
        //Catches the size of the database before evaluating rules
        int initialSize = databaseSize();

        //Evaluates each rule
        for(int i : setSCC){
          cout << r[i].toString() << endl;
          ruleLogic(i);
        }
        passes++;

        //Checks for additional tuples; if none have been added, exit the loop
        int finalSize = databaseSize();
        if(initialSize == finalSize){
          cout << passes << " passes: " << ss.str() << endl;
          adding = false;
        }
      }
    while(adding);
  }
}

void Database::ruleLogic(int i){
  //cout << "ruleLogic(" << i << ")";
  vector<Predicate> preds = r[i].getPredicates();
  vector<Relation> rels = {};

  //Adds relations that match those required by the rule
  for(unsigned int j = 0; j < preds.size(); j++){
    Relation dude = evaluateQueries(preds[j]);
    rels.push_back(dude);
  }

  //Joins 1st and 2nd relations in list, until only one relation remains
  while(rels.size() > 1){
    rels[0] = rels[0].join(rels[1]);
    rels.erase(rels.begin()+1);
  }

  //Adds parameters of schemes that match those of the rule to vector
  Tuple relsScheme = rels[0].getScheme();
  vector<int> indeces = getIndeces(r[i].getHeadPredicate().getParameters(), relsScheme);

  //Displays pertinent info in the correct order
  rels[0] = rels[0].project(indeces);

  //Joins the database's relation with the compiled relation
  mapRelations[r[i].getHeadPredicate().getID()].laUnion(rels[0]);

}

//Compiles indeces of desired columns into a vector
vector<int> Database::getIndeces(vector<Parameter> ruleParameters, Tuple relsScheme){
  vector<int> indeces;
  for(unsigned int i = 0; i < ruleParameters.size(); i++){
    for(unsigned int j = 0; j < relsScheme.size(); j++){
      if(ruleParameters[i].toString() == relsScheme[j]){
        indeces.push_back(j);
      }
    }
  }
  return indeces;
}

//Counts every tuple in the database
int Database::databaseSize(){
  int size = 0;
  for(pair<string,Relation> p : mapRelations){size += p.second.factsSize();}
  return size;
}

//Searches database for tuples that match query
Relation Database::evaluateQueries(Predicate q){
  Relation tempHold;

    //Chooses one of two select functions, based on scheme of query
  chooseSelect(q, tempHold);

  vector<Parameter> queryParameters = q.getParameters();

  //Displays only pertinent columns of data
  vector<int> index = getIndecesQueries(queryParameters);
  tempHold = tempHold.project(index);

  //Assembles new scheme, by taking everything but strings from query's scheme
  Tuple chico;
  for(unsigned int x = 0; x < queryParameters.size(); x++){
    if(queryParameters[x].toString().at(0) != '\''){chico.push_back(queryParameters[x].toString());}
  }
  tempHold.rename(chico);

  //Prevents printing during rule evaluation
  if(!ruling){tempHold.qPrint(q);}

  return tempHold;
}

//Adds indeces of non-string parameters to vector
vector<int> Database::getIndecesQueries(vector<Parameter> params){
  vector<int> index;
  for(unsigned int i = 0; i < params.size(); i++){
    bool found = false;
    if(params[i].toString().at(0) != '\''){
      for(unsigned int j = 0; j<i; j++){
        if(params[i].toString() == params[j].toString()){
          found = true;
        }
      }
      if(!found){index.push_back(i);}
    }
  }
  return index;
}

//Determines which select function will be called
void Database::chooseSelect(Predicate q, Relation &tempHold){
  tempHold = mapRelations[q.getID()];

  vector<Parameter> params = q.getParameters();
  for(unsigned int i = 0; i<params.size(); i++){
    string eval = params[i].toString();
    //Searches for tuples with the same string at the same position
    if(eval.at(0) == '\''){
      tempHold.select(i,eval);
    }
    //Searches for tuples with equal values at the given indeces
    else{
      for(unsigned int j = i+1; j < params.size(); j++){
        string eval2 = params[j].toString();
        if(eval == eval2){
          tempHold.selectar(i,j);
        }
      }
    }
  }
}
