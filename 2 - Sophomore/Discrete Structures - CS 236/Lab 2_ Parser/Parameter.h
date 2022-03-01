#ifndef Parameter_h
#define Parameter_h

#include <iostream>
#include <string>
#include <map>

using namespace std;

//Parent / Template
class Parameter{
public:
  Parameter(string value) : value(value) {}
  string toString();

private:
  string value;
};



#endif /* Parameter_h */
