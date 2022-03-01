using namespace std;

#include "Scanner.h"
#include <ctype.h>

using namespace std;

void Scanner::scan(string file){
  line = 1;
  char check;
  for(i = 0; i < file.size(); i++){
    check = file.at(i);
    switch (check) {
      //COMMA
      case ',':{
        Token dude = Token(COMMA, ",", line);
        myVector.push_back(dude);
        break;
      }
      //PERIOD
      case '.':{
        Token dude = Token(PERIOD, ".", line);
        myVector.push_back(dude);
        break;
      }
      //Q_MARK
      case '?':{
        Token dude = Token(Q_MARK, "?", line);
        myVector.push_back(dude);
        break;
      }
      //LEFT_PAREN
      case '(':{
        Token dude = Token(LEFT_PAREN, "(", line);
        myVector.push_back(dude);
        break;
      }
      //RIGHT_PAREN
      case ')':{
        Token dude = Token(RIGHT_PAREN, ")", line);
        myVector.push_back(dude);
        break;
      }
      //MULTIPLY
      case '*':{
        Token dude = Token(MULTIPLY, "*", line);
        myVector.push_back(dude);
        break;
      }
      //ADD
      case '+':{
        Token dude = Token(ADD, "+", line);
        myVector.push_back(dude);
        break;
      }
      case '\n':{
        line++;
        break;
      }
      default:
        evaluate(file);
    }
  }
  Token dude = Token(TEOF, "", line);
  myVector.push_back(dude);
}

void Scanner::evaluate(string file){
  char check = file.at(i);
  switch (check){
    //COLON / COLON_DASH
    case ':':{
      if(file.at(i+1) == '-'){
        Token dude = Token(COLON_DASH, ":-", line);
        myVector.push_back(dude);
        i++;//to avoid reevaluating the dash
      }
      else{
        Token dude = Token(COLON, ":", line);
        myVector.push_back(dude);
      }
      break;
    }
    //COMMENT
    case '#':{
      comments(file);
      break;
    }
    //STRING
    case '\'':{
      quotes(file);
      break;
    }
    //SCHEMES / ID
    case 'S':{
      keywords(file);
      break;
    }
    //FACTS / ID
    case 'F':{
      keywords(file);
      break;
    }
    //RULES / ID
    case 'R':{
      keywords(file);
      break;
    }
    //QUERIES / ID
    case 'Q':{
      keywords(file);
      break;
    }
    default:{
      makeID(file);
      break;
    }
  }
}

void Scanner::keywords(string file){
  string test = "";
  while(isalpha(file.at(i))||isdigit(file.at(i))){
    test += file.at(i);
    i++;
  }
  char first = test.at(0);

  switch (first){
    case 'S':{
      if(test == "Schemes"){
        Token dude = Token(SCHEMES, test, line);
        myVector.push_back(dude);
      }
      else{
        Token dude = Token(ID, test, line);
        myVector.push_back(dude);
      }
      break;
    }
    case 'F':{
      if(test == "Facts"){
        Token dude = Token(FACTS, test, line);
        myVector.push_back(dude);
      }
      else{
        Token dude = Token(ID, test, line);
        myVector.push_back(dude);
      }
      break;
    }
    case 'R':{
      if(test == "Rules"){
        Token dude = Token(RULES, test, line);
        myVector.push_back(dude);
      }
      else{
        Token dude = Token(ID, test, line);
        myVector.push_back(dude);
      }
      break;
    }
    case 'Q':{
      if(test == "Queries"){
        Token dude = Token(QUERIES, test, line);
        myVector.push_back(dude);
      }
      else{
        Token dude = Token(ID, test, line);
        myVector.push_back(dude);
      }
      break;
    }
  }
  i--;
}

void Scanner::comments(string file){
  char length = 's';

  if(file.at(i+1) == '|'){length = 'l';}  //single line comment, or no

  switch(length){
    case 's':{
      string test = "";
      while(file.at(i) != '\n'){
        test += file.at(i);
        i++;
      }
      Token dude = Token(COMMENT, test, line);
      myVector.push_back(dude);
      i--;//to avoid skipping a char
      break;
    }
    case 'l':{
      multilineComment(file);
      break;
    }
  }
}

void Scanner::multilineComment(string file){
  string test = "";
  int templine = line;
  file.push_back('~');//used instead of file.size(), to avoid out_of_range throws
  string scout = "#";
  unsigned int s = i+1;
  char state = 'r';

  multilineEval(file, scout, s, state);

  file.pop_back();//gets rid of ~

  //COMMENT
  if(state == 'g'){
    scout += '#';
    Token dude = Token(COMMENT, scout, templine);
    myVector.push_back(dude);
    i = s;
  }
  //UNDEFINED
  else{
    while(i < file.size()){
      test += file.at(i);
      i++;
    }
    Token dude = Token(UNDEFINED, test, templine);
    myVector.push_back(dude);
    i--;
  }
}

void Scanner::multilineEval(string file, string& scout, unsigned int& s, char& state){

  //EVALUATING
  while(state == 'r'){
    //no hashes, not EOF
    if(file.at(s) != '#' && file.at(s) != '~'){
      if(file.at(s) == '\n'){line++;}//increment master line
      scout += file.at(s);
      s++;
    }
    //Hash, or EOF
    else{
      if(file.at(s) == '#'){
        // cout << "file at " << s-1 << " is " << file.at(s-1) << endl;
        if(file.at(s-1) == '|'){
          state = 'g';
          break;
        }
        else{
          scout += file.at(s);
          s++;
        }
      }
      else if(file.at(s) == '~'){
        state = 'b';
        break;
      }
    }
  }
}

void Scanner::quotes(string file){
  string scout = "\'";
  unsigned int s = i+1;//to avoid evaluating the initial apostrophe
  int templine = line;
  bool done = false;
  while(!done){
    if(file.at(s) == '\n'){line++;}

    if(s == file.size()-1){
      done = true;
    }
    else if(file.at(s) == '\''){
      if(file.at(s+1) != '\''){
        done = true;
      }
      else{
        scout += file.at(s);
        s++;
        scout += file.at(s);//avoids reevaluating the second apostrophe
        s++;
      }
    }
    else{
      scout += file.at(s);
      s++;
    }
  }
  if(s == file.size()-1){
    scout += file.at(s);//adds the last character of the file
    Token dude = Token(UNDEFINED, scout, templine);
    myVector.push_back(dude);
    //s--;//avoids consuming one too many characters
  }
  else{
    scout += "\'";
    Token dude = Token(STRING, scout, templine);
    myVector.push_back(dude);
  }

  i=s;//master iterator catches up to temporary iterator
}

void Scanner::makeID(string file){
  string scout = "";
  int s = i;
  if(isalpha(file.at(s))){
    while(isalpha(file.at(s)) || isdigit(file.at(s))){
      scout += file.at(s);
      s++;
    }
    Token dude = Token(ID, scout, line);
    myVector.push_back(dude);
    i=s-1;//to reach the immediate next character
  }
  else if(!isspace(file.at(s))){
    string bork = "";
    bork += file.at(s);
    Token dude = Token(UNDEFINED, bork, line);
    myVector.push_back(dude);
  }
}


void Scanner::print(){
  for(unsigned int i = 0; i<myVector.size(); i++){
    myVector[i].print();
  }
  cout << "Total Tokens = " << myVector.size();
}
