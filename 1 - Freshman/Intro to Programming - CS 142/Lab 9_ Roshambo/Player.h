#include <iostream>
#include <string>
#include <sstream>

using namespace std;

class Player{

private:
    string name;
    int wins;
    int losses;
    int draws;
    
public:
    //---------------------------------------------------------------------------------------
    /*
     * Constructor/Destructor
     *
     * Handles creation and deletion of Player objects.
     *
     * Parameter: name
     *		The name of a new player
     * Parameter: wins
     *      The win count of a new player
     * Parameter: losses
     *      The loss count of a new player
     * Parameter: draws
     *      The draw count of a new player
     */
    Player(string name_in, int wins_in, int losses_in, int draws_in);
    virtual ~Player();
    
    /*
     * getName
     *
     * Returns the name of the player.
     *
     * Return:
     *		The name of the player
     */
    string getName();
    
    /*
     * getWins
     *
     * Returns the wins of the Player.
     *
     * Return:
     *		The wins of the player
     */
    int getWins();
    
    /*
     * getLosses
     *
     * Returns the losses of the Player.
     *
     * Return:
     *		The losses of the player
     */
    int getLosses();
    
    /*
     * getDraws
     *
     * Returns the draws of the Player.
     *
     * Return:
     *		The draws of the player
     */
    int getDraws();
    
    /*
     * incrementWins
     *
     * Increments the win count of the player
     */
    void incrementWins();

    /*
     * incrementLosses
     *
     * Increments the loss count of the player
     */
    void incrementLosses();
    
    /*
     * incrementDraws
     *
     * Increments the draw count of the player
     */
    void incrementDraws();
    /*
     * getWinRecord
     *
     * Returns the win record (# of wins / # of rounds) of the Player.
     *
     * Return:
     *		The win record of the player
     */
    double getWinRecord();
    
    /*
     * getRPSThrow
     *
     * Returns the "throw" (rock, paper, or scissors) of the Player.
     *
     * Return:
     *		The "throw" of the player
     */
    string getRPSThrow();
    
    /*
     * toString
     *
     * Returns a single string containing useful information about the car.
     *
     * Return:
     *		A data string about this car
     */
    string toString();
};