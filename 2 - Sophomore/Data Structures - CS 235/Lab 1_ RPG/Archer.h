#ifndef Archer_h
#define Archer_h
#include "Fighter.h"

class Archer:public Fighter{
public:
    Archer(string name, int maxHP, int strength, int speed, int magic) :
    Fighter(name, maxHP, strength, speed, magic) {
        currentSpeed = speed;
    }

    //how much damage the fighter will do
    int getDamage();

    //sets newSpeed to Speed
    void reset();

    //returns currentSpeed
    int getSpeed() const;

    void takeDamage(int damage);

    //++currentSpeed
    bool useAbility();

private:
    int currentSpeed;
};


#endif /* Archer_h */
