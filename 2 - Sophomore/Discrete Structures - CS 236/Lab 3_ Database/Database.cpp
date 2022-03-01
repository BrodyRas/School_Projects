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

//Using facts and schemes, queries are answered
void Database::evaluateQueries(){
  //cout << "evaluateQueries()" << endl;
  for(mi = 0; mi < q.size(); mi++){

    chooseSelect();

    vector<int> index;
    vector<Parameter> params = q[mi].giveParameters();
    Tuple goodParams;
    set<string> check;

    //find only the indeces with variables
    for(unsigned int i=0; i<params.size(); i++){
      string eval = params[i].toString();
      if(check.find(eval) == check.end()){
        if(eval.at(0) != '\''){
          index.push_back(i);
          goodParams.push_back(eval);
          check.insert(eval);
          //cout << eval << " added" << endl;
        }
      }
    }

    tempHold.project(index);
    tempHold.rename(goodParams);
    tempHold.print(q[mi]);
  }
}

//determines which select function will be called
void Database::chooseSelect(){
  tempHold = mapRelations[q[mi].getID()];
  vector<Parameter> params = q[mi].giveParameters();
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
