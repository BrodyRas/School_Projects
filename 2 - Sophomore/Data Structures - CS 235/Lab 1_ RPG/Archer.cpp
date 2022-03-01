#include "Archer.h"

int Archer::getDamage(){
    return currentSpeed;
}

void Archer::reset(){
    Fighter::reset();
    currentSpeed = speed;
}

int Archer::getSpeed() const{
    return currentSpeed;
}

void Archer::takeDamage(int damage){
    damage -= (currentSpeed/4);
    if(damage <= 0){damage = 1;} //assures that damage will be done
    HP -= damage;
}

bool Archer::useAbility(){
    currentSpeed++;
    return true;
}
