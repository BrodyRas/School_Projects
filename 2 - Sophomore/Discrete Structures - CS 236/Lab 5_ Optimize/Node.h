#ifndef Node_h
#define Node_h

#include <vector>
#include <string>
#include <stack>
#include <iostream>
#include <sstream>

using namespace std;

class Node{
public:
  Node(){
    visited = false;
    reflexive = false;
  }

  bool isVisited(){return visited;}

  void visit(){visited = true;}
  void unvisit(){visited = false;}

  void reflex(){
    reflexive = true;
  }

  bool isReflexive(){
    return reflexive;
  }

  set<int> getChildren(){
    return children;
  }

  void addChild(int index){
    children.insert(index);
  }

private:
  bool visited, reflexive;
  set<int> children;
};

#endif /* Node_h */
