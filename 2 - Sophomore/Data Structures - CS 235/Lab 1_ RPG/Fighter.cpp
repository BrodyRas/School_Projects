#include "Fighter.h"


string Fighter::getName() const{
    return name;
}

int Fighter::getMaximumHP() const{
    return maxHP;
}

int Fighter::getCurrentHP() const{
    return HP;
}

int Fighter::getStrength() const{
    return strength;
}

int Fighter::getSpeed() const{
    return speed;
}

int Fighter::getMagic() const{
    return magic;
}

void Fighter::takeDamage(int damage){
    damage -= (speed/4);
    if(damage <= 0){damage = 1;} //assures that damage will be done
    HP -= damage;
}

void Fighter::reset(){
    HP = maxHP;
}

void Fighter::regenerate(){
    int x = strength/6;
    if(x < 1){x = 1;}//ensures the health will be raised by at least one
    HP += x;
    if(HP > maxHP){HP = maxHP;}//if current health exceeds maximum, sets to maximum
}
