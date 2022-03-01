/*
            Test Cases:
 
 1: 13 people, with a 5 percent tip
    1 large, 2 mediums
    Total Area: 716.283 sq in
    Area/Person: 55.09 sq in per person
    Cost: $40
 
 2: 43 people, with a 20 percent tip
    6 large, 1 small
    Total Area: 1998.05 sq in
    Area/Person: 46.46 sq in per person
    Cost: $114

 3: 30 people, with a 15 percent tip
    4 large, 2 small
    Total Area: 1482.83 sq in
    Area/Person: 49.4277 sq in per person
    Cost: $84
*/

#include <iostream>
#include <string>

using namespace std;

int main(){
    const double pi = 3.14159;
    int people;
    int tip;
    
    const double LARGE_AREA = (pi*100);
    const double MEDIUM_AREA = (pi*64);     //<< constant area of various pizza sizes
    const double SMALL_AREA = (pi*36);
    
    const double LARGE_COST = 14.68;
    const double MEDIUM_COST = 11.48;       //<< constant cost of various pizza sizes
    const double SMALL_COST = 7.28;
    
    const int COUNT_LARGE = 7;              //how many people a large will feed
    const int COUNT_MEDIUM = 3;             //how many people a medium will feed
    
    
    
    cout << "How many people are attending?\n";
    cin >> people;
    cout << "\nAnd what percentage  tip?\n%";
    cin >> tip;
    
    
    int numLarge = people/COUNT_LARGE;      //total large pizzas
    int remLarge = people%COUNT_LARGE;      //remainder of people going to medium
    
    int numMedium = remLarge/COUNT_MEDIUM;  //total medium pizzas
    int remMedium = remLarge%COUNT_MEDIUM;  //remainder of people going to small
    
    int numSmall = remMedium;               //any leftover people recieve a small

    double area = (numLarge*LARGE_AREA)+(numMedium*MEDIUM_AREA)+(numSmall*SMALL_AREA);
    double areaPerPerson = area/people;
    
    double cost = (numLarge*LARGE_COST)+(numMedium*MEDIUM_COST)+(numSmall*SMALL_COST);
    const float tipTotal = (cost*tip)/100;
    int finalcost = (cost+tipTotal)+.5;

    cout << "\nWe will need " << numLarge << " large pizzas, " << numMedium << " medium pizzas, and " << numSmall << " small pizzas.\n";
    
    cout << "\nPeople attending: " << people;
    cout << "\nTip percentage: %" << tip;
    cout << "\nTotal Area of Pizza: " << area << " square inches";
    cout << "\nArea per Person: " << areaPerPerson << " square inches per person";
    cout << "\nTotal cost: $" << finalcost << endl;
    
    return 0;
}