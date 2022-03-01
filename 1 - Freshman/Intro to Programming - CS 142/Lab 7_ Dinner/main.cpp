/**
 Test Cases:
 
 
 */

#include <iostream>
#include <iomanip>
#include <vector>
#include <string>
#include <stdio.h>
#include <cmath>
#include <ctime>

using namespace std;

//clears invalid inputs, and returns new value
int input_error(int value){
    cout << "Please make a valid choice.\nChoice - ";
    cin.clear();
    cin.ignore(1000, '\n');
    cin >> value;
    return value;
}

//ensures blank inputs aren't stored as names
string name_error(vector<string>& restaurants, string name){
    cout << "\nPlease enter a name: ";
    getline(cin, name);//accept the whole line as name
    return name;
}

//provide vector with 8 restaurants
void initialize(vector<string>& restaurants){
    string input;
    input = "McDonald's";
    restaurants.push_back (input);
    input = "Burger King";
    restaurants.push_back (input);
    input = "Jamba Juice";
    restaurants.push_back (input);
    input = "Chick-Fil-A";
    restaurants.push_back (input);
    input = "Wendy's";
    restaurants.push_back (input);
    input = "Pizza Hut";
    restaurants.push_back (input);
    input = "Olive Garden";
    restaurants.push_back (input);
    input = "Arby's";
    restaurants.push_back (input);
}

//Display all restaurants
void print(vector<string>& restaurants){
    cout << "\nList of Restaurants:\n";
    for(int i = 1; i <= restaurants.size(); ++i){
    cout << restaurants[i-1];       //print all elements within vector
        if(i<restaurants.size()){
             cout << ", ";          //prevents floating comma
        }
    }
    cout << endl;
}

//find restaurant name to check for duplicates
int find(vector<string>& restaurants, string name){
    for(int i = 1; i <= restaurants.size(); ++i){
        if(name==restaurants[i-1]){
            return i-1;  //if the input matches the name of a restaurant in the vector, return it's index
        }
    }
    return -1;  //if name isn't in vector, return -1
}

//Add a restaurant to the vector, if it isn't already there
void add(vector<string>& restaurants){
    string name = "";
    
    getline(cin, name);
    while(name == ""||name == " "){
        name = name_error(restaurants, name);
    }
    int not_repeat = find(restaurants, name);
    if(not_repeat!=-1){
        cout << "\nThis restaurant is already in the vector. It won't be added twice.\n";
    }//refuse duplicate inputs
    else{
    cout << "\nAdding " << name << " to vector!\n";
    restaurants.push_back (name);//add string to the end of the vector
    }
}

//Remove a restaurant
void remove(vector<string>& restaurants){
    string name;
    string temp;

    cout << "\nChoose which restaurant to remove:\n";
    for(int i = 1; i <= restaurants.size(); ++i){
        cout << "- " << restaurants[i-1] << endl;//print all elements within vector
    }
    cout << "\nChoice: ";
    getline(cin, name);
    getline(cin, name); //accept whole line as name

    
    int index = find(restaurants, name);
    
    //if name is in vector, remove it
    if(index!=-1){
    cout << "\nRemoved restaurant: " << name << endl;
        
        restaurants.erase(restaurants.begin()+index);
    }
    //if name isn't in vector, do nothing
    else{cout<<"\nThis name isn't in the list.\n";}
}

//Shuffle and print the vector
void shuffle(vector<string>& restaurants){
    srand(time(0));
    int index;
    string temp;
    
    for(int z = 0; z < 50; ++z){
        index = rand()%(restaurants.size()-1);                      //find a random index
        temp = restaurants[z%(restaurants.size()-1)];               //store name to be replaced in temp
        restaurants[z%(restaurants.size()-1)] = restaurants[index]; //replace temp'd name with random index name
        restaurants[index] = temp;                                  //assign replaced name to random index
    }
    
    cout << "\nThe restaurants have been shuffled.\n";
    print(restaurants);
}

//BEGIN THE BLOODBATH (returns whether the program should continue or not)
bool tournament(vector<string>& restaurants){
    bool isTourneyWorthy = false;//true if the number of restaurants is 2^x
    int size = 0;
    int factor = 1;
    
    //checks size of vector, to ensure it works for a tourney structure (2^x)
    for(int x=0; x<1; ++x){
        size = pow(2, factor);//size is equal to 2 to the power of the number of iterations
        if(restaurants.size()>=size){//only check while the size checker doesn't exceed the size of the vector
            if(restaurants.size()==size){
                isTourneyWorthy = true;
                ++x;
            }
        }
        else{
            ++x;
        }
        ++factor;
        --x;
    }
    
    //START TOURNEY
    if(isTourneyWorthy){
        int match_size = restaurants.size()/2;//each round pits 2 competetors against each other; initial number of rounds equals half the initial competetors
        int round_size = log(restaurants.size())/log(2);//number of rounds equals the exponent to which 2 must be raised to equal the number of restaurants
        int choice;
        
        
        
        for(int j = 1; j <= round_size; ++j){
            for(int i = 1; i <=match_size; ++i){
                cout << "Match: " << i << "/" << match_size << " | Round " << j << "/" << round_size;
                cout << " | 1: " << restaurants[i-1] << " or 2: " << restaurants[i] << "? Choice - ";
                cin >> choice;
                
                //ensure the input is either 1 or 2
                while((choice!=1&&choice!=2) || cin.fail()){
                    choice = input_error(choice);
                }
                
                //eliminate losing restaurant
                if(choice==1){
                    restaurants.erase(restaurants.begin()+i);
                }
                //
                else{
                    restaurants.erase(restaurants.begin()+(i-1));
                }
            }
            //change match size to accomodate remaining number of restaurants
            match_size = restaurants.size()/2;
        }
        
        cout << "\nWe have a winner!\nWe're going to " << restaurants[0];
        
        return false;
    }
    
    //INVALID NUMBER OF RESTAURANTS
    else{
        cout << "\nThere are not an appropriate number of restaurants for a tournament.\n";
        return true;
    }
}

int main() {
    bool play = true;
    int menu_choice;
    
    vector<string> restaurants;
    
    //provide vector with 8 restaurants
    initialize(restaurants);
    
    cout << "Welcome to the Restaurant Tournament!";
    
    while(play == true){
    
    cout << "\n1 - Display all restaurants\n2 - Add a restaurant\n3 - Remove a restaurant\n4 - Shuffle the vector\n5 - Begin the tournament\n6 - Quit the program\n\nChoice - ";
    
    cin >> menu_choice;
    
    //Don't proceed until a valid input is given
    while(menu_choice<1||menu_choice>6||cin.fail()){
        menu_choice = input_error(menu_choice);
    }
        if(menu_choice==1){
            print(restaurants);
        }
        else if(menu_choice==2){
            add(restaurants);
        }
        else if(menu_choice==3){
            remove(restaurants);
        }
        else if(menu_choice==4){
            shuffle(restaurants);
        }
        else if(menu_choice==5){
            //tourney function ends loop if tourney is successful
            play = tournament(restaurants);
        }
        else if(menu_choice==6){
            play = false;//leave the program
        }
    }
    
    cout << "\nLet's eat!\n\n";
    
    return 0;
}
