#ifndef Scanner_h
#define Scanner_h

#include "Token.h"
#include <iostream>
#include <string>
#include <vector>
#include <map>

using namespace std;

class Scanner{
public:
  Scanner(){}

  void scan(string file);
  void evaluate(string file);
  void keywords(string file);
  void comments(string file);
  void multilineComment(string file);
  void multilineEval(string file, string& scout, unsigned int& s, char& state);
  void quotes(string file);
  void makeID(string file);
  void print();
  vector<Token> giveVector();


private:
  int line;
  unsigned int i;
  char next;
  vector<Token> myVector;
};


#endif /* Scanner_h */
