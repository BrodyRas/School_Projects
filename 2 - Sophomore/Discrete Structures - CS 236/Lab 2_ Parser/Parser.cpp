#include "Parser.h"
#include "Token.h"
#include <ctype.h>
#include <iostream>

using namespace std;

void Parser::getVector(vector<Token> vector){
  myVector = vector;  //picks up vector of tokens from Scanner
  building = false;
  //removes comments, so parser doesn't have to see them
  for(unsigned int j = 0; j < myVector.size(); j++){
    if(myVector[j].getType() == COMMENT){
      myVector.erase(myVector.begin() + j);
      j--;
    }
  }

}

//assure that the file has proper syntax
void Parser::parse(){
  i = 0;
  try{
    runDatalogProgram();
    cout << "Success!\n";
    youngKing.print();
  }
  catch(Token token){
    cout << "Failure!\n  ";
    token.print();
  }
}

///////////////////////////////////////////////////////
void Parser::runDatalogProgram(){
  //cout << "runDatalogProgram()~~~~~~~~" << endl;

  match(SCHEMES);
  match(COLON);
  scheme();
  schemeList();

  match(FACTS);
  match(COLON);
  factList();

  match(RULES);
  match(COLON);
  ruleList();

  match(QUERIES);
  match(COLON);
  query();
  queryList();
}

///////////////////////////////////////////////////////
void Parser::schemeList(){
  //cout << "schemeList()~~~~~~~~" << endl;
  if(myVector[i].getType() == ID){
    scheme();
    schemeList();
  }
}

void Parser::factList(){
  //cout << "factList()~~~~~~~~" << endl;
  if(myVector[i].getType() == ID){
    fact();
    factList();
  }
}

void Parser::ruleList(){
  //cout << "ruleList()~~~~~~~~" << endl;
  if(myVector[i].getType() == ID){
    rule();
    ruleList();
  }
}

void Parser::queryList(){
  //cout << "queryList()~~~~~~~~" << endl;
  if(myVector[i].getType() != TEOF){
    query();
    queryList();
  }
}

///////////////////////////////////////////////////////
Predicate Parser::scheme(){
  //cout << "scheme()~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" << endl;
  Predicate doge = headPredicate();
  youngKing.addScheme(doge);
  return doge;
}

Predicate Parser::fact(){
  //cout << "fact()~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" << endl;
  tempParam.clear();
  string boi = myVector[i].getValue();
  match(ID);
  match(LEFT_PAREN);
  Parameter first(myVector[i].getValue());
  tempParam.push_back(first);
  match(STRING);
  stringList();
  match(RIGHT_PAREN);
  match(PERIOD);
  Predicate facto(boi, tempParam);
  youngKing.addFact(facto);
  return facto;
}
                                                                   //HELP NEEDED
////////////////////////////////////////////////////////////////////////////////
Rule Parser::rule(){
  //cout << "rule()~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" << endl;
  tempPred.clear();
  Predicate cabeza = headPredicate();
  match(COLON_DASH);
  predicate();
  predicateList();
  match(PERIOD);
  Rule bonzo(cabeza, tempPred);
  youngKing.addRule(bonzo);
  return bonzo;
}

Predicate Parser::query(){
  //cout << "query()~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" << endl;
  Predicate pred = predicate();
  match(Q_MARK);
  youngKing.addQuery(pred);
  return pred;
}
////////////////////////////////////////////////////////////////////////////////

Predicate Parser::headPredicate(){
  //cout << "headPredicate()~~~~~~~~" << endl;
  tempParam.clear();
  string boi = myVector[i].getValue();
  match(ID);
  match(LEFT_PAREN);
  Parameter dude(myVector[i].getValue());
  tempParam.push_back(dude);
  match(ID);
  IDList();
  match(RIGHT_PAREN);
  Predicate pred(boi, tempParam);
  return pred;
}

Predicate Parser::predicate(){
  //cout << "predicate()~~~~~~~~" << endl;
  tempParam.clear();
  string boi = myVector[i].getValue();
  match(ID);
  match(LEFT_PAREN);
  parameter();
  parameterList();
  match(RIGHT_PAREN);
  Predicate pred(boi, tempParam);
  tempPred.push_back(pred);
  return pred;
}

///////////////////////////////////////////////////////
void Parser::predicateList(){
  //cout << "predicateList()~~~~~~~~" << endl;
  if(myVector[i].getType() == COMMA){
    match(COMMA);
    predicate();
    predicateList();
  }
}

void Parser::parameterList(){
  //cout << "parameterList()~~~~~~~~" << endl;
  if(myVector[i].getType() != RIGHT_PAREN){
    match(COMMA);
    parameter();
    parameterList();
  }
}

void Parser::stringList(){
  //cout << "stringList()~~~~~~~~" << endl;
  if(myVector[i].getType() != RIGHT_PAREN){
    match(COMMA);
    Parameter first(myVector[i].getValue());
    tempParam.push_back(first);
    match(STRING);
    stringList();
  }
}

void Parser::IDList(){
  //cout << "IDList()~~~~~~~~" << endl;
  if(myVector[i].getType() != RIGHT_PAREN){
    match(COMMA);
    Parameter temp(myVector[i].getValue());
    tempParam.push_back(temp);
    match(ID);
    IDList();
  }
}

///////////////////////////////////////////////////////
void Parser::parameter(){
  //cout << "parameter()~~~~~~~~" << endl;
  string boi = myVector[i].getValue();

  tempParam.push_back(boi);

  if(myVector[i].getType() == STRING){
    match(STRING);
  }
  else if(myVector[i].getType() == ID){
    match(ID);
  }
  else{
    expression();
    tempParam.pop_back();       //erases the (
    Parameter dude(exp_str);
    tempParam.push_back(dude);  //adds the constructed expression to vector
    exp_str.clear();  //prepares string for possible future expressions
  }
}

void Parser::expParameter(){
  //cout << "expParameter()~~~~~~~~" << endl;


  if(myVector[i].getType() == STRING){
    exp_str += myVector[i].getValue();
    match(STRING);
  }
  else if(myVector[i].getType() == ID){
    exp_str += myVector[i].getValue();
    match(ID);
  }
  else{
    expression();
  }
}


void Parser::expression(){
  //cout << "expression()~~~~~~~~" << endl;
  exp_str += "(";
  match(LEFT_PAREN);
  expParameter();
  operators();
  expParameter();
  exp_str += ")";
  match(RIGHT_PAREN);
}

//************************************************************************************CHECK ORS
void Parser::operators(){
  //cout << "operators()~~~~~~~~" << endl;
  exp_str += myVector[i].getValue();
  if(myVector[i].getType() == ADD){
    match(ADD);
  }
  else if(myVector[i].getType() == MULTIPLY){
    match(MULTIPLY);
  }
  else{
    throw(myVector[i]); //if it's anything but * or +, fail
  }
}

///////////////////////////////////////////////////////
void Parser::match(TType type){
  map<TType,string> myMap = Token::getMap();
  //myVector[i].print();

  //cout << "E: " << myMap[type] << "\nR: " << myMap[myVector[i].getType()] << endl;

  if(myVector[i].getType()!=type){
    throw myVector[i];
  }
  else{
    i++;
  }
}
