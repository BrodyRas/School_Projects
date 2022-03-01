#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <set>
#include <map>
#include <vector>
#include <list>

using namespace std;

int main(){
//***********************************************************************Part 1
  ifstream ifs;
  ofstream ofs;
  string name;
  set<string>::iterator ssi;
  set<string> master;
  cout << "Input: ";
  cin >> name;

  ifs.open(name + ".txt");
  ofs.open(name + "_set.txt");

  string parse;

  while (ifs>>parse){
    for(int i=0; i<parse.size(); i++){
        if(!isalpha(parse.at(i))){
          parse.erase(parse.begin() + i);
        }
    }
    master.insert(parse);
}
int i = 0;
for(ssi=master.begin(); ssi!=master.end(); ssi++, i++){
  ofs << *ssi << " ";
  ofs << endl;
}

cout << "\nPart 1:" << endl << name << "_set.txt written!" << endl;
cout << "Set size: " << master.size() << endl;

ifs.close();
ofs.close();

//***********************************************************************Part 2
  vector<string> blaster;
  vector<string>::iterator vsi;
  ifs.open(name + ".txt");
  ofs.open(name + "_vector.txt");

  while (ifs>>parse){
    for(i=0; i<parse.size(); i++){
        if(!isalpha(parse.at(i))){
          parse.erase(parse.begin() + i);
        }
    }
    blaster.push_back(parse);
}
for(vsi=blaster.begin(); vsi!=blaster.end(); vsi++, i++){
  ofs << *vsi << " ";
  ofs << endl;
}

cout << "\nPart 2:" << endl << name << "_vector.txt written!" << endl;
cout << "Vector size: " << blaster.size() << endl;

ifs.close();
ofs.close();

//***********************************************************************Part 3
//Caught in a bad loop

map<string, string> wordmap;
map<string, string>::iterator mssi;
string last = "";

for(vsi=blaster.begin();vsi!=blaster.end(); vsi++){
  wordmap[last] = *vsi;
  last = *vsi;
}

ofs.open(name + "_1_1.txt");

for(mssi=wordmap.begin(); mssi!=wordmap.end();mssi++){
  ofs << mssi->first << ", " << mssi->second << endl;
}

cout << "\nPart 3:" << endl << name << "_1_1.txt written!" << endl;

ofs.close();

//***********************************************************************Part 4
cout << "\nPart 4:\n";
last = "";
for (int i = 0; i < 100; i++){
  cout << wordmap[last] << " ";
  last = wordmap[last];
  if(i%30 == 0 && i != 0){
    cout << endl;
  }
}
cout << endl;

//***********************************************************************Part 5
map<string, vector<string>> wordmap2;
last = "";
i = 0;
for(vsi=blaster.begin(); vsi!=blaster.end(); vsi++){
  wordmap2[last].push_back(*vsi);
  last = *vsi;
}

srand(time(NULL));

cout << "\nPart 5:\n";

last = "";
for(int i = 0; i < 100; i++){
  int ind = rand() % wordmap2[last].size();
  cout << wordmap2[last][ind] << " ";
  last = wordmap2[last][ind];
  if(i%30 == 0 && i != 0){
    cout << endl;
  }
}

//***********************************************************************Part 6
map<list<string>, vector<string>> wordmap3;
list<string> state, newPhone;

cout << "\n\nPart 6:\n";

for(i=0; i<2; i++){
  state.push_back("");
  newPhone.push_back("");
}

for(vsi=blaster.begin(); vsi!=blaster.end(); vsi++){
  wordmap3[state].push_back(*vsi);
  state.push_back(*vsi);
  state.pop_front();
}

for(int i = 0; i < 100; i++){
  int ind = rand() % wordmap3[newPhone].size();
  cout << wordmap3[newPhone][ind] << " ";
  newPhone.push_back(wordmap3[newPhone][ind]);
  newPhone.pop_front();
  if(i%30 == 0 && i != 0){
    cout << endl;
  }
}

cout << endl << endl;
return 0;
}
