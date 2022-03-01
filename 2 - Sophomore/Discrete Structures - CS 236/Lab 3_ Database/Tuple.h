#ifndef Tuple_h
#define Tuple_h

#include <vector>
#include <string>
#include <iostream>
#include <sstream>

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
};

#endif /* Tuple_h */
