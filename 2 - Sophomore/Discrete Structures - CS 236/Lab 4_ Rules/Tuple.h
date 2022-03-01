#ifndef Tuple_h
#define Tuple_h

#include <vector>
#include <string>
#include <iostream>
#include <sstream>

using namespace std;

class Tuple : public std::vector<std::string> {
public:
  std::string toString(){
    std::stringstream ss;
    for(unsigned int i = 0; i < size(); i++){
      ss << at(i);
      if(i<size()-1){
        ss << ",";
      }
    }
    return ss.str();
  }
  void rPrint(Tuple scheme){
    //cout << "TUPLE size: " << size() << " SCHEME size: " << scheme.size() << endl;
    cout << "  ";
    for(unsigned int i = 0; i < scheme.size(); i++){
      cout << scheme[i] << "=" << at(i);
      if(i<scheme.size()-1){
        cout << ", ";
      }
    }
    cout << endl;
  }
};

#endif /* Tuple_h */
