#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <fstream>
#include "Pathfinder.h"

using namespace std;

	//Part 1-----------------------------------------------------------------------------------
	/*
	* toString
	*
	* Returns a string representation of the current maze. Returns a maze of all 1s if no maze
	* has yet been generated or imported.
	*
	* A valid string representation of a maze consists only of 125 1s and 0s (each separated
	* by spaces) arranged in five 5x5 grids (each separated by newlines) with no trailing newline. A valid maze must
	* also have 1s in the entrance cell (0, 0, 0) and in the exit cell (4, 4, 4).
	*
	* Cell (0, 0, 0) is represented by the first number in the string representation of the
	* maze. Increasing in x means moving toward the east, meaning cell (1, 0, 0) is the second
	* number. Increasing in y means moving toward the south, meaning cell (0, 1, 0) is the
	* sixth number. Increasing in z means moving downward to a new grid, meaning cell
	* (0, 0, 1) is the twenty-sixth cell in the maze. Cell (4, 4, 4) is represented by the last number.
	*
	* Returns:		string
	*				A single string representing the current maze
	*/
	string Pathfinder::toString() const{

	  stringstream ss;

	  //funnels array into stringstream
	  for(int k=0; k<5; k++){
	    for(int j=0; j<5; j++){
	      for(int i=0; i<5; i++){
	          ss << theMaze[i][j][k];    //adds current element of array to stringstream
	 	        if(i!=4){
	          ss << " "; //adds space if not the last (POSSIBLY NOT NECESSARY?)
	        }
	      }
	      ss << endl; //stringstreams can take endlines?
	    }
	    if(k!=4){ss << endl;} //stringstreams are so dope
	  }
	  return ss.str();
	}

	/*
	* createRandomMaze
	*
	* Generates a random maze and stores it as the current maze.
	*
	* The generated maze must contain a roughly equal number of 1s and 0s and must have a 1
	* in the entrance cell (0, 0, 0) and in the exit cell (4, 4, 4).  The generated maze may be
	* solvable or unsolvable, and this method should be able to produce both kinds of mazes.
	*/
	void Pathfinder::createRandomMaze(){
	  srand(time(NULL));

	  //fills array with 0's and 1's
	  for(int i=0; i<5; i++){
	    for(int j=0; j<5; j++){
	      for(int k=0; k<5; k++){
	        theMaze[i][j][k] = rand() % 2;
	      }
	    }
	  }
		//ensures entrance and exit are open
		theMaze[0][0][0] = 1;
		theMaze[4][4][4] = 1;
	}

	//Part 2-----------------------------------------------------------------------------------
	/*
	* importMaze
	*
	* Reads in a maze from a file with the given file name and stores it as the current maze.
	* Does nothing if the file does not exist or if the file's data does not represent a valid
	* maze.
	*
	* The file's contents must be of the format described above to be considered valid.
	*
	* Parameter:	file_name
	*				The name of the file containing a maze
	* Returns:		bool
	*				True if the maze is imported correctly; false otherwise
	*/
	bool Pathfinder::importMaze(string file_name){
		ifstream ifs;
	  string next;
		int testArray[5][5][5]; //if a test array isn't used, the current maze will be edited until proven invalid
	  ifs.open(file_name);

	  if(ifs.fail()){
	    return false; //File not found
	  }

	  for(int k=0; k<5; k++){
	    for(int j=0; j<5; j++){
	      for(int i=0; i<5; i++){
	        if(ifs >> next){
						if(next.size()>1){return false;}  //ensures each element is an isolated character
						testArray[i][j][k] = next[0] - 48;  //returns number character to its acutal numeric value, adds to array
					}
	        else{
	          return false; //stringstream expended, not enough input
	        }
	      }
	    }
	  }

	  //seeing if there's anything left in the infilestream
	  string testString = "";
	  ifs >> testString;
	  //checking for extra input
	  if(testString != ""){
	    //too much input
	    return false;
	  }

	  //ensures that entrance and exit are open
	  if(testArray[0][0][0] != 1 || testArray[4][4][4] != 1){
	    return false; //bad entrance/exit
	  }


		for(int i=0; i<5; i++){
			for(int j=0; j<5; j++){
				for(int k=0; k<5; k++){
					theMaze[i][j][k] = testArray[i][j][k];
				}
			}
		}

	  //File found, maze added
	  return true;
  }

	bool Pathfinder::findPath(int x, int y, int z){
	stringstream ss;
	ss << "(" << x << ", " << y << ", " << z << ")";
	P.push_back(ss.str());
	//                  outsize of maze                        blocked cell             already been there
	if((x < 0 || x > 4 || y < 0 || y > 4 || z < 0 || z > 4) || theMaze[x][y][z] == 0 || theMaze[x][y][z] == 2){
		P.pop_back();
		return false;
	}
	//end of maze, baby
	if(x == 4 && y == 4 && z == 4){
		return true;
	}

	theMaze[x][y][z] = 2;

	if(findPath(x+1, y, z) || findPath(x-1, y, z) || findPath(x, y+1, z) || findPath(x, y-1, z) || findPath(x, y, z+1) || findPath(x, y, z-1)){
		return true;
	}
	else{
		P.pop_back();
		return false;
	}
}

	/*
	* solveMaze
	*
	* Attempts to solve the current maze and returns a solution if one is found.
	*
	* A solution to a maze is a list of coordinates for the path from the entrance to the exit
	* (or an empty vector if no solution is found). This list cannot contain duplicates, and
	* any two consecutive coordinates in the list can only differ by 1 for only one
	* coordinate. The entrance cell (0, 0, 0) and the exit cell (4, 4, 4) should be included
	* in the solution. Each string in the solution vector must be of the format "(x, y, z)",
	* where x, y, and z are the integer coordinates of a cell.
	*
	* Understand that most mazes will contain multiple solutions
	*
	* Returns:		vector<string>
	*				A solution to the current maze, or an empty vector if none exists
	*/
	vector<string> Pathfinder::solveMaze(){
		P.clear();
		if(findPath(0, 0, 0) == true){
			for(int i=0; i<5; i++){
				for(int j=0; j<5; j++){
					for(int k=0; k<5; k++){
						if(theMaze[i][j][k]==2){theMaze[i][j][k]=1;}
					}
				}
			}
			return P;
		}
		else{
			P.clear();
			return P;
		}
  }
