/*
 Test Cases:
 1.
 
 
 2.
 
 
 3.
 
 
 */
#include <iostream>
#include <string>
#include <vector>
#include <iomanip>
#include <fstream>
#include "Car.h"

const double STARTING_BALANCE = 10000;
const int NOT_FOUND = -1;

using namespace std;

//reprompts for input if a non-int, or a bad int, are inputted
int int_error(int input){
    cout << "Please make a valid choice\nChoice - ";
    cin.clear();
    cin.ignore(1000, '\n');
    cin >> input;
    return input;
}

//evaluates vector, and returns index of matching name, or -1 if no name matches
int findName(vector<Car> inventory, string name){
    
    for (int i = 0; i < inventory.size(); ++i){
        if (name == inventory[i].getName()){
            return i;
        }
    }
    return NOT_FOUND;
}

//adds one car to inventory, and removes price from balance
void addCar(vector<Car>& inventory, double& balance){
    
    string name;
    string color;
    double price;
    
    bool duplicate = false;
    
    cout << "\nWhat type of car is it?\nName - ";
    getline(cin, name);	//doubling input prompt prevents accepting newline as input
    getline(cin, name);
    
    for (int i = 0; i < inventory.size(); ++i){
        if (name == inventory[i].getName()){
            duplicate = true;
            break;
        }
    }
    
    if (duplicate){
        cout << "\nWe already have a car with that name!\n";
    }
    else{
        cout << "What color is the car?\nColor - ";
        getline(cin, color);
        cout << "How much does the car cost?\nPrice $";
        cin >> price;
        
        while (price < 1){	//prevents invalid inputs
            price = int_error(price);
        }
        
        if (price>balance){
            //won't buy if you don't have enough cheddar
            cout << "\nYou don't have enough money to buy that!\n";
        }
        
        else{
            //removes price from balance, adds car to vector, and tells user what's happening
            cout << "\nBuying a " << color << " " << name << " for $" << price << ".\n";
            balance -= price;
            Car input_car = Car(name, color, price);
            inventory.push_back(input_car);
        }
    }
}

//removes one car from inventory, and adds price to balance
void removeCar(vector<Car>& inventory, double& balance){
    string name;
    cout << "\nWhich car would you like to sell? - ";
    cin >> name;
    
    int found = findName(inventory, name);
    
    if (found == NOT_FOUND){
        //you can't sell a car you don't own
        cout << "\nYou don't own a car with that name!\n";
    }
    else{
        //adds price to balance, removes car from vector, and tells user what's happening
        cout << "\nSelling your " << name << " for $" << inventory[found].getPrice() << ".\n";
        balance += inventory[found].getPrice();
        inventory.erase(inventory.begin() + found);
    }
}

//changes both the color and price of one car in inventory
void paintCar(vector<Car>& inventory){
    cout << "\nWhich car do you want to repaint?\nName - ";
    string name;
    getline(cin, name);
    getline(cin, name);
    
    int found = findName(inventory, name);
    
    if (found == NOT_FOUND){
        cout << "\nYou don't own a car by that name!\n";
    }
    else{
        cout << "What color would you like to paint it?\nColor - ";
        string color;
        getline(cin, color);
        
        inventory[found].paint(color);
        
        cout << "We've painted your " << name << " a beautiful " << color << ". Now, it's worth $" << inventory[found].getPrice() << "\n";
    }
}

//loads balance and inventory from file
void loadInventory(vector<Car>& inventory, double& balance){
    ifstream in;
    cout << "What inventory would you like to load: ";
    string file;
    cin >> file;
    in.open(file.c_str());
    //read info
    double new_balance;
    string name, color;
    double price;
    
    in >> new_balance;
    
    balance += new_balance;
    
    while (!in.eof()){
        in >> name >> color >> price;
        Car new_one = Car(name, color, price);
        inventory.push_back(new_one);
    }
    
    in.close();
    
}

//saves inventory to a file
void saveInventory(vector<Car>& inventory, double balance){
    ofstream out;
    cout << "Where would you like to save your inventory: ";
    string file;
    cin >> file;
    out.open(file.c_str());
    
    out << balance << endl;
    
    for (int i = 0; i < inventory.size(); ++i){
        out << inventory[i].getName() << " " << inventory[i].getColor() << " " << inventory[i].getPrice();
        if (i < inventory.size() - 1){
            cout << endl;
        }
    }
    
    out.close();
    
}



int main() {
    int choice;
    bool play = true;
    
    vector<Car> inventory;
    double balance = STARTING_BALANCE;
    
    cout << "-----------------------------------------------------------------\n";
    cout << "Welcome to Crazy Jimmy's Bargain-priced Pre-owned Pinto Emporium!\n";
    cout << "-----------------------------------------------------------------";
    
    while (play){
        cout << "\n1 - Show Current Inventory\n2 - Show Current Balance\n3 - Buy a Car\n4 - Sell a Car\n5 - Paint a Car\n6 - Load File\n7 - Save File\n8 - Quit Program\n\nChoice - ";
        cin >> choice;
        
        while (choice<1 || choice>8 || cin.fail()){
            choice = int_error(choice);
        }
        
        if (choice == 1){
            //print current inventory
            if (inventory.size() == 0){
                cout << "\nYou don't own any cars yet!\n";
            }
            
            for (int i = 0; i < inventory.size(); i++){
                cout << inventory[i].toString();
            }
        }
        
        else if (choice == 2){
            //print current balance
            cout << "\nCurrent balance: $" << balance << endl;
        }
        else if (choice == 3){
            addCar(inventory, balance);
        }		
        else if (choice == 4){
            removeCar(inventory, balance);
        }
        else if (choice == 5){
            paintCar(inventory);
        }
        else if (choice == 6){
            loadInventory(inventory, balance);
        }
        else if (choice == 7){
            saveInventory(inventory, balance);
        }
        else if (choice == 8){
            play = false;
        }
    }
    
    cout << "\nThank you, come again!\n\n";
    
    return 0;
}
