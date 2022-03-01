#include <stdio.h>
#include <iostream>
#include <string>
#include "LinkedList.h"

using namespace std;

int main(int argc, char *argv[]) {
    LinkedList<string> linked_listo;
    
    linked_listo.append("The");//"yes");
    linked_listo.append("professor");//"no");
    linked_listo.append("is");//"maybe");
    linked_listo.append("awesome");//"so");

    linked_listo.print();
    //cout << "The size is: " << linked_listo.size() << endl;
    
    linked_listo.insert(3, "NOT");
    linked_listo.print();
    
    linked_listo.set(3, "VERY");
    linked_listo.print();
    //cout << "Wait, what is the professor? " << linked_listo.get(5) << endl;
    
    return 0;
}


