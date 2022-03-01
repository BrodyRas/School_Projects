#ifndef Token_h
#define Token_h

#include <iostream>
#include <sstream>
#include <string>
#include <map>


using namespace std;

enum TType{
  COMMA,	//The ',' character
  PERIOD,	//The '.' character
  Q_MARK,	//The '?' character
  LEFT_PAREN,	//The '(' character
  RIGHT_PAREN,	//The ')' character
  COLON,	//The ':' character
  COLON_DASH,	//The string ":-"
  MULTIPLY,	//The '*' character
  ADD,	//The '+' character
  SCHEMES,	//The string "Schemes"
  FACTS,	//The string "Facts"
  RULES,	//The string "Rules"
  QUERIES,	//The string "Queries"
  ID,	//A letter followed by zero or more letters or digits, and is not a keyword (Schemes, Facts, Rules, Queries).
  STRING,	//A sequence of characters enclosed in single quotes. White space (space, tab) is not skipped when inside a string.
  COMMENT,	//A hash character (#) and ends at the end of the line or end of the file.	# This is a comment
  UNDEFINED,	//Any character not tokenized as a string, keyword, identifier, symbol, or white space is undefined.
  TEOF	//The end of the input file
};

class Token{
public:
  Token(TType type, string value, int lineNum);
  void initialize();
  TType getType();
  string getValue();
  static map<TType,string>& getMap();
  void print();

private:
  int lineNum;
  string value;
  TType type;

  static bool isInitialized;
  static map<TType,string> myMap;
};



#endif /* Token_h */
