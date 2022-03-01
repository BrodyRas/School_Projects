#ifndef Fighter_h
#define Fighter_h

#include "FighterInterface.h"

using namespace std;

class Fighter:public FighterInterface{

public:
    Fighter(string name, int maxHP, int strength, int speed, int magic) {
        this->name = name;
        this->maxHP = maxHP;
        HP = maxHP;
        this->strength = strength;
        this->speed = speed;
        this->magic = magic;
    }

    //returns the fighter's name
    virtual string getName() const;

    //returns the fighter's maximum HP
    virtual int getMaximumHP() const;

    //returns the fighter's current HP
    virtual int getCurrentHP() const;
    
    //returns the fighter's strength
    virtual int getStrength() const;

    //returns the fighter's speed
    virtual int getSpeed() const;

    //returns the fighter's magic
    virtual int getMagic() const;

    //how much damage the fighter will do
    virtual int getDamage() = 0;

    //damage - (speed/4)
    virtual void takeDamage(int damage);

    //sets HP to maxHP
    virtual void reset();

    //HP + (strength/6)
    virtual void regenerate();

    //activates the fighter's ability
    virtual bool useAbility() = 0;

protected:
    string name;
    int maxHP;
    int HP;
    int strength;
    int speed;
    int magic;
};


#endif /* Fighter_h */
