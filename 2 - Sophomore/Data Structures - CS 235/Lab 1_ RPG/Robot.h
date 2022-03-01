#ifndef Robot_h
#define Robot_h

#include "Fighter.h"

class Robot:public Fighter{
public:
    Robot(string name, int maxHP, int strength, int speed, int magic) :
    Fighter(name, maxHP, strength, speed, magic) {
        maxEnergy = magic*2;
        energy = maxEnergy;
        bonus = 0;
    }

//how much damage the Robot will do
int getDamage();

//energy = maxEnergy; bonus = 0;
void reset();

//activate the boost
bool useAbility();

private:
    int maxEnergy;
    int energy;
    int bonus;
};


#endif /* Robot_h */
