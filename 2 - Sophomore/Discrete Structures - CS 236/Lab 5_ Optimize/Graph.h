#ifndef Graph_h
#define Graph_h

#include "Node.h"
#include <vector>
#include <string>
#include <iostream>
#include <sstream>

using namespace std;

class Graph{
public:
  Graph(){
    SCCing = false;
  }

  //Creates node, adds it to graph with given index
  void addNode(int a){
    Node nuNode = Node();
    pair<int,Node> nuPair = make_pair(a,nuNode);
    dependencyGraph.insert(nuPair);
    myBoys.push_back(a);
  }

  //Declares node a's dependcency on node b
  void assignDependency(int a, int b){
    dependencyGraph[a].addChild(b);
    if(a==b){dependencyGraph[a].reflex();}
  }

  //Calls DFS on each node, to assure none get left behind
  void DFSForest(){
    //cout << "DFSForest()\n";
    for(unsigned int i; i < dependencyGraph.size(); i++){
      if(!dependencyGraph[i].isVisited()){
        runDFS(i);
      }
      //cout << "attempting to jump to " << i+1 << "...\n";
    }

    //cout << "Printing stack: "; stackPrint();

  }

  //Reaches the deepest path of tree, pushes indeces onto stack while retreating
  void runDFS(int i){
    //cout << "runDFS(" << i << ")\n";
    dependencyGraph[i].visit();
    set<int> children = dependencyGraph[i].getChildren();
    //Searches through all unvisited children recursively
    for(int j : children){
      if(!dependencyGraph[j].isVisited()){
        //Adds the unvisited child to the SCC
        if(SCCing){
          //cout << "Adding child " << j << " to temp...\n";
          temp.insert(j);
        }
        //cout << "visiting children...\n";
        runDFS(j);
      }
    }
    //The stack only is affected if the graph isn't searching for SCC's
    if(!SCCing){
      //Adds the node's index to the stack, after visiting all it's children
      //cout << "Pushing " << i << " to stack\n";
      postorder.push(i);
    }
  }

  //Searches the graph for strongly connected components
  void findSCC(stack<int> posts){
    SCCing = true;
    //cout << "findSCC()--------------------------\n";
    while(!posts.empty()){
      //Adds index to set only if the node hasn't been visited
      if(!dependencyGraph[posts.top()].isVisited()){
        temp.insert(posts.top());
        runDFS(posts.top());
        //cout << "Adding temp to CSS's...";
        //for(int j : temp) {cout << j << " ";}
        //cout << endl;
        SCC.push_back(temp);
      }

      //cout << "About to pop " << posts.top() << "...\n";
      posts.pop();
      temp.clear();
    }

    //Print the vector of sets of ints (SCC's)
    //for(unsigned int i = 0; i < SCC.size(); i++){cout << "{ ";for(int j : SCC[i]) {cout << j << " ";}cout << "}  ";}
    //cout << endl;

  }

  vector<set<int>> getSCC(){return SCC;}

  //Gives stack of indeces, based on a postorder traversal of the graph
  stack<int> getPostorder(){return postorder;}

  //Debugging///////////////////////////////////////////////////////////////////
  void unvisitGraph(){
    for(unsigned int i; i < dependencyGraph.size(); i++){
      cout << "unvisiting " << i << "...\n";
      dependencyGraph[i].unvisit();
    }
  }
  void stackPrint(){
    stack<int> temp = postorder;
    while(!temp.empty()){
      cout << temp.top() << " ";
      temp.pop();
    }
    cout << endl;
  }
  //////////////////////////////////////////////////////////////////////////////

  bool isReflexive(int i){
    return dependencyGraph[i].isReflexive();
  }

  //Prints each node, followed by each of it's children
  void print(){
    for(pair<int,Node> p : dependencyGraph){
      set<int> children = p.second.getChildren();
      cout << "R" << p.first << ":";
      unsigned int temp = 0;
      for(int i : children){
        cout << "R" << i;
        if(temp < children.size()-1){cout << ",";}
        temp++;
      }
      cout << endl;
    }
  }

private:
  map<int,Node> dependencyGraph;
  vector<int> myBoys;
  stack<int> postorder;

  vector<set<int>> SCC;
  bool SCCing;
  set<int> temp;
};

#endif /* Graph_h */
