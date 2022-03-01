/*
 
Test Cases:
 1.
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Quit the program
 
 Choice: f
 
 INVALID:
 Please choose 1, 2, or 3
 
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Quit the program
 
 Choice: 1
 
 SINGLE:
 Please select which slot to drop your chip [0-8]
 Choice: f
 
 INVALID:
 Please choose a slot number between 0 and 8
 
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Quit the program
 
 Choice: 1
 
 SINGLE:
 Please select which slot to drop your chip [0-8]
 Choice: 5
 
 ***
 Dropping the chip into slot 5
 Path: [5.0 5.5 5.0 4.5 5.0 5.5 6.0 6.5 7.0 6.5 6.0 6.5 7.0]
 Winnings: $500.00
 ***
 
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Quit the program
 
 Choice: 3
 
 GOODBYE

    2.
    MENU:
    Welcome to the Plinko Simulator!
    Please choose your operation:
    1 - Drop a single chip into one slot
    2 - Drop multiple chips into one slot
    3 - Quit the program
 
    Choice: 2
 
    MULTIPLE:
    Please input how many chips will be dropped [<0]
    Choice: f
 
    INVALID:
    Please input a number over 0
 
    MENU:
    Welcome to the Plinko Simulator!
    Please choose your operation:
    1 - Drop a single chip into one slot
    2 - Drop multiple chips into one slot
    3 - Quit the program
 
    Choice: 2
 
    MULTIPLE:
    Please input how many chips will be dropped [<0]
    Choice: 23
 
    Please select which slot to drop your chip [0-8]
    Choice: f
 
    INVALID:
    Please choose a slot number between 0 and 8
 
    MENU:
    Welcome to the Plinko Simulator!
    Please choose your operation:
    1 - Drop a single chip into one slot
    2 - Drop multiple chips into one slot
    3 - Quit the program
 
    Choice: 2
 
    MULTIPLE:
    Please input how many chips will be dropped [<0]
    Choice: 23
 
    Please select which slot to drop your chip [0-8]
    Choice: 4
 
    ***
    Dropping 23 chips into slot 4
    Total winnings from 23 chips: $46500.00
    Average winnings per chip: $2021.74
    ***
 
    MENU:
    Welcome to the Plinko Simulator!
    Please choose your operation:
    1 - Drop a single chip into one slot
    2 - Drop multiple chips into one slot
    3 - Quit the program
 
    Choice: 3
 
    GOODBYE
 
 3.
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Drop a certain number of chips into all slots
 4 - Quit the program
 
 Choice: 3
 ALL SLOTS: Please choose how many chips to drop into each slot:
 Chips [<0]: fe
 
 INVALID:
 Please choose a number greater than 0
 
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Drop a certain number of chips into all slots
 4 - Quit the program
 
 Choice: 3
 ALL SLOTS: Please choose how many chips to drop into each slot:
 Chips [<0]: -5
 
 INVALID:
 Please choose a number greater than 0
 
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Drop a certain number of chips into all slots
 4 - Quit the program
 
 Choice: 3
 ALL SLOTS: Please choose how many chips to drop into each slot:
 Chips [<0]: 10
 Dropping 10 chips into all the slots...
 Average winnings for Slot 0: $430.00
 Average winnings for Slot 1: $2510.00
 Average winnings for Slot 2: $1600.00
 Average winnings for Slot 3: $1700.00
 Average winnings for Slot 4: $1260.00
 Average winnings for Slot 5: $2300.00
 Average winnings for Slot 6: $5210.00
 Average winnings for Slot 7: $480.00
 Average winnings for Slot 8: $1480.00
 
 Total Winnings: $169700.00
 
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Drop a certain number of chips into all slots
 4 - Quit the program
 
 Choice: 4
 
 GOODBYE
*/

#include <iomanip>
#include <iostream>
#include <ctime>

using namespace std;

double singleDrop(double slotChoice);
double multiDrop(double chipCount, double slotChoice);
double winDaMoney(double position);
void weird_drop(int num_chips);
double position_assign(double position);


//string input in main menu, total for each slot, not overall

int main(){
    srand(time(0));
    int userChoice = 1;
    double slotChoice;
    double numChips;
    
    
    while ((userChoice >= 1 && userChoice < 4)){
        
        cout << "MENU:\nWelcome to the Plinko Simulator!\nPlease choose your operation:\n1 - Drop a single chip into one slot\n2 - Drop multiple chips into one slot\n3 - Drop a certain number of chips into all slots\n4 - Quit the program\n\nChoice: ";                //Initial prompt
        cin >> userChoice;
        
        if(cin.fail() || userChoice<1 || userChoice > 4){
            cout << "\nINVALID:\nPlease choose 1, 2, or 3\n\n";
            cin.clear();
            cin.ignore(1000, '\n');
        } //Until the user imputs a valid value, don't proceed
        
        
        if (userChoice == 1){
            cout << endl << "SINGLE:\n" << "Please select which slot to drop your chip [0-8]\nChoice: ";
            cin >> slotChoice;                                          //Prompt user for slot choice
            
            if(cin.fail() || slotChoice<0 || slotChoice>8){
                cout << "\nINVALID:\nPlease choose a slot number between 0 and 8\n\n";
                cin.clear();
                cin.ignore(1000, '\n');
            } //Slot choice must be valid
            else{
                cout << "\n***\nDropping the chip into slot " << slotChoice << endl << "Path: [" << fixed << setprecision(1) << slotChoice << " ";
                double position = slotChoice;
                for(int counter=1; counter <=12; counter++){
                    
                    position = singleDrop(position);

                    
                    cout << fixed << setprecision(1) << position;
                    if(counter<12){
                        cout << " ";
                    }
                }
                cout << "]\n\n";
                
                double prizeMoney = winDaMoney(position);
                
                cout << "Winnings: $" << fixed << setprecision(2) << prizeMoney << "\n***\n\n";
            }
        }           //Drop Single Chip
        else if (userChoice == 2){
            cout << endl << "MULTIPLE:\nPlease input how many chips will be dropped [<0]\nChoice: ";
            cin >> numChips;                                                //Prompt for number of chips
            
            if(cin.fail() || numChips<=0){
                cout << "\nINVALID:\nPlease input a number over 0\n\n";
                cin.clear();
                cin.ignore(1000, '\n');
            } //Chip count must be positive
            else{
                cout << endl << "Please select which slot to drop your chip [0-8]\nChoice: ";
                cin >> slotChoice;                                          //Prompt user for slot choice
                
                if(cin.fail() || slotChoice<0 || slotChoice>8){
                    cout << "\nINVALID:\nPlease choose a slot number between 0 and 8\n\n";
                    cin.clear();
                    cin.ignore(1000, '\n');
                }                                                           //Slot choice must be valid
                else{
                    cout << "\n***\n" << fixed << setprecision(0) << "Dropping " << numChips << " chips into slot " << slotChoice << endl;
                    
                    double prizeSum = multiDrop(numChips, slotChoice);
                    
                    cout << "Total winnings from " << numChips << " chips: $" << fixed << setprecision(2) << prizeSum << endl;      //Sum of all individual wins
                    cout << "Average winnings per chip: $" << fixed << setprecision(2) << prizeSum/numChips << "\n***\n\n";              //Prize money divided by chips used
                }
            }
        }      //Drop Multiple Chips
        else if (userChoice == 3){
            int chip_count;
            cout << "ALL SLOTS: Please choose how many chips to drop into each slot:\nChips [<0]: ";
            cin >> chip_count;
            
            if(cin.fail() || chip_count<1){
                cout << "\nINVALID:\nPlease choose a number greater than 0\n\n";
                cin.clear();
                cin.ignore(1000, '\n');
            }
            else{
                weird_drop(chip_count);
            }
        }
        else if(userChoice == 4){}          //if the user input is 3, move outside of while loop
        else{userChoice = 1;}               //else, set user choice to 1, begin the loop again
    }
    cout << "\nGOODBYE\n";              //bye-bye
    
    return 0;
}

const int WIN_0 = 100;
const int WIN_1 = 500;
const int WIN_2 = 1000;
const int WIN_3 = 0;
const int WIN_4 = 10000;        //PRIZE MONEY VALUES^^vv
const int WIN_5 = 0;
const int WIN_6 = 1000;
const int WIN_7 = 500;
const int WIN_8 = 100;




double singleDrop(double position){

    int random = rand()%2;
    
    if(position==8){
        position-=.5;
    }
    else if(position==0){
        position+=.5;
    }
    
    else if(random==0){
        position+=.5;
    }
    else if (random==1){
        position-=.5;
    }
    return position;
}


double multiDrop(double numChips, double slotChoice){
    
    
    
    double prizeSum = 0;
    int multiCounter = numChips; //
    
    for(multiCounter; multiCounter>=0; multiCounter--) {
        
        double position = slotChoice;
        for(int counter=1; counter <=12; counter++){
            position = singleDrop(position);
        }
        
        double prizeMoney = winDaMoney(position);
        
        prizeSum = prizeSum + prizeMoney;
    }
    
    return prizeSum;
    
}

void weird_drop(int num_chips){
    double winnings = 0;
    double add = 0;
    double money[9];
    
    cout << "Dropping " << num_chips << " chips into all the slots...\n";
    
    for(int i = 0; i<=8; i++){
        add = multiDrop(num_chips, i);
        winnings += add;
        money[i] = add;
        cout << "Average winnings for Slot " << i << ": $" << fixed << setprecision(2) << money[i]/num_chips << " - Total Winnings for Slot " << i << ": $" << money[i] << endl;
    }
    
    cout << endl << endl;
}


double winDaMoney(double position){
    
    double prizeMoney = position;

    
    if(position==0){
        prizeMoney = WIN_0;
    }
    else if(position==1){
        prizeMoney = WIN_1;
    }
    else if(position==2){
        prizeMoney = WIN_2;
    }
    else if(position==3){
        prizeMoney = WIN_3;
    }
    else if(position==4){
        prizeMoney = WIN_4;
    }
    else if(position==5){
        prizeMoney = WIN_5;
    }
    else if(position==6){
        prizeMoney = WIN_6;
    }
    else if(position==7){
        prizeMoney = WIN_7;
    }
    else if(position==8){
        prizeMoney = WIN_8;
    }
    
    
    return prizeMoney;
}


