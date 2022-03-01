#include "Arena.h"
#include "Fighter.h"
#include "Archer.h"
#include "Cleric.h"
#include "Robot.h"

#include <vector>
#include <string>
#include <iostream>
#include <sstream>

bool Arena::addFighter(string info){
    string name;
    char type;
    int maxHP;
    int strength;
    int speed;
    int magic;
    
    stringstream ss;
    ss << info;
    
    //reads info into ss
    if(ss >> name >> type >> maxHP >> strength >> speed >> magic){
        
        //ensures there isn't a duplicate name
        for(int i=0; i<myArena.size(); i++){
            if(name == myArena[i]->getName()){
                return false;
            }
        }
        
        //checks for "extra parameters"
        if(ss.peek() != EOF){
            return false;
        }
        
        //assigns class based on type
        if(type == 'A'){//creates a new archer
            myArena.push_back(new Archer(name, maxHP, strength, speed, magic));
            return true;
        }
        else if(type == 'C'){//creates a new Cleric
            myArena.push_back(new Cleric(name, maxHP, strength, speed, magic));
            return true;
        }
        else if(type == 'R'){//creates a new Robot
            myArena.push_back(new Robot(name, maxHP, strength, speed, magic));
            return true;
        }
    }
    return false;
}

bool Arena::removeFighter(string name){
    for(int i=0; i<myArena.size(); i++){
        if(name == myArena[i]->getName()){
            myArena.erase(myArena.begin() + i);
            return true;
        }
    }
    return false;
}

FighterInterface* Arena::getFighter(string name){
    for(int i=0; i<myArena.size(); i++){
        if(name == myArena[i]->getName()){
            return myArena[i];
        }
    }
    return NULL;
}

int Arena::getSize() const{
    return myArena.size();
}
