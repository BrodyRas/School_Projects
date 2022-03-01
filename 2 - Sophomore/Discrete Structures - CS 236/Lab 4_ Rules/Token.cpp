#include "Token.h"

using namespace std;

bool Token::isInitialized = false;
map<TType,string> Token::myMap;

Token::Token(TType type, string value, int lineNum){
  if(!isInitialized){
    initialize();
    isInitialized = true;
  }
  this->type = type;
  this->value = value;
  this->lineNum = lineNum;
}

void Token::initialize(){
  myMap[COMMA]="COMMA";
  myMap[PERIOD]="PERIOD";
  myMap[Q_MARK]="Q_MARK";
  myMap[LEFT_PAREN]="LEFT_PAREN";
  myMap[RIGHT_PAREN]="RIGHT_PAREN";
  myMap[COLON]="COLON";
  myMap[COLON_DASH]="COLON_DASH";
  myMap[MULTIPLY]="MULTIPLY";
  myMap[ADD]="ADD";
  myMap[SCHEMES]="SCHEMES";
  myMap[FACTS]="FACTS";
  myMap[RULES]="RULES";
  myMap[QUERIES]="QUERIES";
  myMap[ID]="ID";
  myMap[STRING]="STRING";
  myMap[COMMENT]="COMMENT";
  myMap[UNDEFINED]="UNDEFINED";
  myMap[TEOF]="EOF";
}

TType Token::getType(){
  return type;
}

string Token::getValue(){
  return value;
}


map<TType,string>& Token::getMap(){
  return myMap;
}


void Token::print(){
  cout << "(" << myMap[type] << ",\"" << value << "\"," << lineNum << ")\n";
}
