#include "Robot.h"
#include <cmath>

int Robot::getDamage(){
    int newStrength = strength + bonus;
    bonus = 0;
    return newStrength;
}

void Robot::reset(){
    Fighter::reset();
    energy = maxEnergy;
    bonus = 0;
}

bool Robot::useAbility(){
    bonus = 0;
    if(energy>=ROBOT_ABILITY_COST){
        bonus = (int)((double)strength  * pow(((double)energy/(double)maxEnergy), 4));
        energy -= ROBOT_ABILITY_COST;
        return true;
    }
    else{return false;}
}
