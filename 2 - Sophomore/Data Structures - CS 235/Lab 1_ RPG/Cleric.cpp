#include "Cleric.h"

int Cleric::getDamage(){
    return magic;
}

void Cleric::reset(){
    Fighter::reset();
    mana = maxMana;
}

void Cleric::regenerate(){
    Fighter::regenerate();
    int add = magic/5;
    if (add<1){add=1;}
    mana += add;
    if(mana>maxMana){mana=maxMana;}
}

bool Cleric::useAbility(){
    if(mana>=CLERIC_ABILITY_COST){
        mana = mana - CLERIC_ABILITY_COST;
        int x = magic/3;
        HP += x;
        if(HP>maxHP){
            Fighter::reset();
        }
        return true;
    }
    else{return false;}
}
