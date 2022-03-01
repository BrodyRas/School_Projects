#ifndef ARENA_HPP_
#define ARENA_HPP_

#include "ArenaInterface.h"
#include "Fighter.h"

#include <iostream>
#include <string>
#include <vector>

using namespace std;

class Arena:public ArenaInterface {
public:
    //adds a fighter to the arena
    virtual bool addFighter(string info);
    
    //removes fighter from the arena
    virtual bool removeFighter(string name);
    
    //finds a fighter with name
    virtual FighterInterface* getFighter(string name);
    
    //returns the size of the arena
    virtual int getSize() const;
    
private:
    vector<Fighter*> myArena;
};

#endif
