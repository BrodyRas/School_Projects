#ifndef Cleric_h
#define Cleric_h

#include "Fighter.h"

class Cleric:public Fighter{
public:
    Cleric(string name, int maxHP, int strength, int speed, int magic) :
    Fighter(name, maxHP, strength, speed, magic) {
        maxMana = magic*5;
        mana = maxMana;
    }

    //how much damage the Cleric will do
    int getDamage();

    //mana = maxMana
    void reset();

    //mana = mana*(magic/5)
    void regenerate();

    //heals the Cleric
    bool useAbility();

private:
    int maxMana;
    int mana;
};


#endif /* Cleric_h */
