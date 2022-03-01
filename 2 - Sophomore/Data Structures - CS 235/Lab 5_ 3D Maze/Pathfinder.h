#pragma once

#include "PathfinderInterface.h"

 using namespace std;

class Pathfinder: public PathfinderInterface
{
public:
	Pathfinder() {
    for(int i=0; i<5; i++){
	    for(int j=0; j<5; j++){
	      for(int k=0; k<5; k++){
						theMaze[i][j][k] = 1;
	        }
	      }
	    }
	  }
	virtual ~Pathfinder() {}

  string toString() const;
  void createRandomMaze();
  bool importMaze(string file_name);
  bool findPath(int x, int y, int z);
  vector<string> solveMaze();

private:
  vector<string> P;
  int theMaze[5][5][5];
};
