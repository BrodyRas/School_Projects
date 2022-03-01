#include <iostream>
#include <vector>
#include <string>
#include <ctime>
#include "Player.h"

const string ROCK = "ROCK";
const string PAPER = "PAPER";
const string SCISSORS = "SCISSORS";

using namespace std;

//reprompts for invalid integer input
int int_error(int choice, string type){
    cout << "\nPlease make a valid choice.\n" << type << ": ";
    cin.clear();
    cin.ignore(1000, '\n');
    cin >> choice;
    return choice;
}

int find(vector<Player*> players, string name){
    for(int i = 0; i<players.size(); ++i){
        if(name==players[i]->getName()){
            return i;
        }
    }
    return -1;
}

//starts off vector with a few players
void initialize(vector<Player*>& players){
    
    Player* new_one;//acts as a template to add new 'players' to the vector
    
    new_one = new Player("Finn", 0, 0, 0);
    players.push_back(new_one);
    new_one = new Player("Jake", 0, 0, 0);
    players.push_back(new_one);
    new_one = new Player("Clarence", 0, 0, 0);
    players.push_back(new_one);
    new_one = new Player("Steven", 0, 0, 0);
    players.push_back(new_one);
}

//takes input, and creates a new player to add to the vector
void add_player(vector<Player*>& players){
    string name;
    
    cout << "\nWho do you want to add to our group?\nName: ";
    getline(cin, name);
    getline(cin, name);

    
    int dupe = find(players, name);
    
    //prevents adding 2 players with the same name
    if(dupe != -1){
        cout << "\nWe already have a " << name << " in our group!\n";
    }
    
    else{
        cout << "\nAdding " << name << " to our group!\n";
        //creates new player from data input, and adds it to vector;
        Player* new_one = new Player(name, 0, 0, 0);
        players.push_back(new_one);
    }
}

//asks for which existing play you wish to add to the vector
void add_lineup(vector<Player*> players_all, vector<Player*>& players_competing){
    string name;
    cout << "\nWhich player do you want to add to the line-up?\nName: ";
    getline(cin, name);
    getline(cin, name);
    
    int found = find(players_all, name);
    
    if(found == -1){
        cout << "\nWe don't have a player of that name!\n";
    }
    else{
        cout << "\nAdding " << name << " to the line-up!\n";

        players_competing.push_back(players_all[found]);//add the found player to the line-up vector;
    }
}

//prints all players in a given vector
void print(vector<Player*> players){
    if(players.size()==0){
        cout << "\nYou don't have any players here!\n";
    }
    else{
        cout << "\nLIST OF ALL PLAYERS\n";
        for(int i = 0; i < players.size(); ++i){
            cout << players[i]->toString();
            if(i!=players.size()-1){
                cout << endl;
            }
        }
    }
}

//START THE BLOODSHED
void start_battle(vector<Player*>& players){
    if(players.size()<2){
        cout << "\nYou don't have enough players in the line-up to start a fight!\n";
    }
    else{
        string player1throw = players[0]->getRPSThrow();
        string player2throw = players[1]->getRPSThrow();
        
        cout << "\nA fight has started between " << players[0]->getName() << " and " << players[1]->getName() << "!\n\n";
        
        if(players[0]->getName()==players[1]->getName()){
            cout << "It's a tie!\n";
            players[0]->incrementDraws();
        }
        else{
            cout << players[0]->getName() << " throws " << player1throw << "!\n";
            cout << players[1]->getName() << " throws " << player2throw << "!\n";
            
            //runs through each option, and assigns points accordingly
            if(player1throw==ROCK){
                if(player2throw==ROCK){
                    cout << "\nIt's a tie!\n";
                    players[0]->incrementDraws();
                    players[1]->incrementDraws();
                }
                else if(player2throw==PAPER){
                    cout << "\n" << players[1]->getName() <<" wins!\n";
                    players[0]->incrementLosses();
                    players[1]->incrementWins();
                }
                else if(player2throw==SCISSORS){
                    cout << "\n" << players[0]->getName() <<" wins!\n";
                    players[1]->incrementLosses();
                    players[0]->incrementWins();
                }
            }
            else if(player1throw==PAPER){
                if(player2throw==PAPER){
                    cout << "\nIt's a tie!\n";
                    players[0]->incrementDraws();
                    players[1]->incrementDraws();
                }
                else if(player2throw==SCISSORS){
                    cout << "\n" << players[1]->getName() <<" wins!\n";
                    players[0]->incrementLosses();
                    players[1]->incrementWins();
                }
                else if(player2throw==ROCK){
                    cout << "\n" << players[0]->getName() <<" wins!\n";
                    players[1]->incrementLosses();
                    players[0]->incrementWins();
                }
            }
            else if(player1throw==SCISSORS){
                if(player2throw==SCISSORS){
                    cout << "\nIt's a tie!\n";
                    players[0]->incrementDraws();
                    players[1]->incrementDraws();
                }
                else if(player2throw==ROCK){
                    cout << "\n" << players[1]->getName() <<" wins!\n";
                    players[0]->incrementLosses();
                    players[1]->incrementWins();
                }
                else if(player2throw==PAPER){
                    cout << "\n" << players[0]->getName() <<" wins!\n";
                    players[1]->incrementLosses();
                    players[0]->incrementWins();
                }
            }
            players.erase(players.begin()+1);
            players.erase(players.begin());
            //removes players from the line-up after fighting
        }
    }
}


int main(){
    
    srand(time(0));
    int choice;
    bool play = true;
    
    vector<Player*> players_all;
    vector<Player*> players_competing;
    
    //initialize(players_all);//start vector with a few players
    
    cout << "-------------------------------------------------\n";
    cout << "WELCOME TO THE ROCK/PAPER/SCISSORS COMPETITION!!!\n";
    cout << "-------------------------------------------------";
    
    while(play){
        cout << "\n1: Show All Players\n2: Add Player\n3: Add to Line-Up\n4: Display Line-Up\n5: Fight\n6: Quit\n\nChoice: ";
        
        cin >> choice;
        
        //prevents invalid inputs
        while(choice<1||choice>6||cin.fail()){
            choice = int_error(choice, "Choice");
        }
        
        if(choice==1){
            print(players_all);
        }
        else if(choice==2){
            add_player(players_all);
        }
        else if(choice==3){
            add_lineup(players_all, players_competing);
        }
        else if(choice==4){
            print(players_competing);
        }
        else if(choice==5){
            start_battle(players_competing);
        }
        else if(choice==6){
            play = false;
        }
    }
    
    cout << "\n-------------------";
    cout << "\nThanks for playing!\n";
    cout << "-------------------\n";
    
    return 0;
}
