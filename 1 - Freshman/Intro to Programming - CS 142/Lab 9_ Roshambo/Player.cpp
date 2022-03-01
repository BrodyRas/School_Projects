#include "Player.h"
#include <string>
#include <iomanip>

const int ROCK_INT = 1;
const int PAPER_INT = 2;
const int SCISSORS_INT = 3;

const string ROCK = "ROCK";
const string PAPER = "PAPER";
const string SCISSORS = "SCISSORS";

using namespace std;

//---------------------------------------------------------------------------------------
Player::Player(string name_in, int wins_in, int losses_in, int draws_in){
    name = name_in;
    wins = wins_in;
    losses = losses_in;
    draws = draws_in;
}
Player::~Player(){}
//---------------------------------------------------------------------------------------
string Player::getName(){
    return name;
}
//---------------------------------------------------------------------------------------
int Player::getWins(){
    return wins;
}
//---------------------------------------------------------------------------------------
int Player::getLosses(){
    return losses;
}
//---------------------------------------------------------------------------------------
int Player::getDraws(){
    return draws;
}
//---------------------------------------------------------------------------------------
void Player::incrementWins(){
    ++wins;
}
//---------------------------------------------------------------------------------------
void Player::incrementLosses(){
    ++losses;
}
//---------------------------------------------------------------------------------------
void Player::incrementDraws(){
    ++draws;
}

double Player::getWinRecord(){
    double win_record;
    double denom = wins+losses+draws;
    
    if(denom==0){
        win_record = 0;
    }
    else{
        win_record = (wins/denom) * 100;
    }
    return win_record;
}
//---------------------------------------------------------------------------------------
string Player::getRPSThrow(){
    string move;
    int random = (rand()%3)+1; //returns 1, 2, or 3 randomly, which represents RPS throws
    
    if(random==ROCK_INT){
        move = ROCK;
    }
    else if(random==PAPER_INT){
        move = PAPER;
    }
    else if(random==SCISSORS_INT){
        move = SCISSORS;
    }
    return move;
}
//---------------------------------------------------------------------------------------
string Player::toString(){
    stringstream ss;
    ss << "Name: " << name << endl;
    ss << "Wins: " << wins << endl;
    ss << "Losses: " << losses << endl;
    ss << "Draws: " << draws << endl;
    ss << "Win Percentage: " << setprecision(4) << getWinRecord() << "%" << endl;
    return ss.str();
}
//---------------------------------------------------------------------------------------