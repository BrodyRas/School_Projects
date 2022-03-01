#include "Database.h"
#include <string>

//Creates relations based on the schemes provided in program
void Database::createRelations(){
  //cout << "createRelations()" << endl;
  vector<Predicate> boi = youngKing.giveSchemes();
  for(unsigned int i = 0; i < boi.size(); i++){
    Relation dude = Relation(boi[i]);
    mapRelations.insert(std::pair<string,Relation>(dude.getName(),dude));
  }
}

//Fills relations with facts
void Database::populateRelations(){
  //cout << "populateRelations()" << endl;
  vector<Predicate> dude = youngKing.giveFacts();
  for(unsigned int i = 0; i < dude.size(); i++){
    mapRelations[dude[i].getID()].addFact(dude[i]);
  }
}

/////////////////////////////////////////////////////////////////////////////////RIGHT HERE, FAM
void Database::evaluateRules(){
  cout << "Rule Evaluation" << endl;
  int passes = 0;

  adding = true;

    do{
      int initialSize = databaseSize();//cout << "Initial Size: " << initialSize << endl;
      //cout << "one\n";
      for(unsigned int i = 0; i < r.size(); i++){
        //cout << "two\n";
        cout << r[i].toString() << endl;
        vector<Predicate> preds = r[i].givePredicates();
        vector<Relation> rels = {};
        for(unsigned int j = 0; j < preds.size(); j++){
          //cout << "j= " << j << "\npreds.size()= " << preds.size() << endl;
          Relation dude = evaluateQueries(preds[j]);
          //dude.rPrint();
          rels.push_back(dude);
        }

        Predicate head = r[i].giveHeadPredicate();

        //continually joins relations in list until only one relation remains
        while(rels.size() > 1){
          //cout << "rels size: " << rels.size() << endl;
          rels[0] = rels[0].join(rels[1]);
          //cout << "joining...\n";
          //rels[0].rPrint();

          rels.erase(rels.begin()+1);
        }

        Tuple relsScheme = rels[0].giveScheme();
        vector<Predicate> news = r[i].givePredicates();


        vector<int> indeces = getIndeces(r[i].giveHeadPredicate().giveParameters(), relsScheme);

        //cout << "R PRE PROJECTvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
        //rels[0].rPrint();
        //cout << "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";

        //Clean that sucker up
        rels[0] = rels[0].project(indeces);

        //cout << "R POST PROJECTvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
        //rels[0].rPrint();
        //cout << "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";

        //rels[0].rename(mapRelations[head.getID()].giveScheme());

        //cout << "R POST RENAMEvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
        //rels[0].rPrint();
        //cout << "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";


        //int preSize = databaseSize();
        mapRelations[head.getID()].laUnion(rels[0]);//Join the database's relation with the compiled relation
        //int postSize = databaseSize();//cout << "postSize: " << postSize << "\n";


        //cout << "R POST UNIONvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
        //if(preSize != postSize){mapRelations[head.getID()].rPrint();}
        //cout << "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";


      }

      int finalSize = databaseSize();//cout << "Final Size: " << finalSize << "\n\n";
      passes++;
      if(initialSize == finalSize){
        adding = false;//if no more tuples have been added, exit the loop
      }
    }
  while(adding);

  cout << "\nSchemes populated after " << passes << " passes through the Rules.\n\n";
}

vector<int> Database::getIndeces(vector<Parameter> ruleParameters, Tuple relsScheme){
  vector<int> indeces;
  //cout << "master relation scheme size: " << relsScheme.size() << endl;
  //cout << "ruleParameters.size(): " << ruleParameters.size() << endl;
  for(unsigned int i = 0; i < ruleParameters.size(); i++){
    //cout << "ruleParameters[i] == " << ruleParameters[i].toString();
    for(unsigned int j = 0; j < relsScheme.size(); j++){
      //cout << " " << relsScheme[j] << ", ";
      if(ruleParameters[i].toString() == relsScheme[j]){
        indeces.push_back(j);
      }
    }
    //cout << endl;
  }

  return indeces;
}

int Database::databaseSize(){
  int size = 0;
  for(pair<string,Relation> p : mapRelations){size += p.second.factsSize();}
  return size;
}


//Using facts, rules, and schemes, queries are answered
Relation Database::evaluateQueries(Predicate q){

    Relation tempHold;
    chooseSelect(q, tempHold);
    //cout << "tempHold scheme size 2: " << tempHold.giveScheme().size() << endl;

    vector<Parameter> params = q.giveParameters();

    Tuple key = tempHold.giveScheme();

    vector<int> index = getIndecesQueries(params);

    vector<Parameter> boi = q.giveParameters();
    Tuple chico;

    for(unsigned int x = 0; x < boi.size(); x++){
      if (boi[x].toString().at(0) != '\'') {
        chico.push_back(boi[x].toString());
      }
    }


    //cout << "Q PRE RENAMEvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
    //tempHold.rPrint();
    //cout << "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";


    tempHold = tempHold.project(index);


    //cout << "Q POST PROJECTvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
    //tempHold.rPrint();
    //cout << "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";


    tempHold.rename(chico);//NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO


    //cout << "Q POST RENAMEvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\n";
    //tempHold.rPrint();
    //cout << "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n";


    if(!ruling){tempHold.qPrint(q);}  //prevents printing during rule evaluation

  return tempHold;
}

vector<int> Database::getIndecesQueries(vector<Parameter> params){
  //find only the indeces with variables
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

//determines which select function will be called
void Database::chooseSelect(Predicate q, Relation &tempHold){
  //cout << "chooseSelect()\n" << q.getID() << endl;
  tempHold = mapRelations[q.getID()];
  //cout << "tempHold scheme size: " << tempHold.giveScheme().size() << endl;

  //tempHold.rPrint();

  vector<Parameter> params = q.giveParameters();
  for(unsigned int i = 0; i<params.size(); i++){
    string eval = params[i].toString();
    if(eval.at(0) == '\''){
      tempHold.select(i,eval);
    }
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
