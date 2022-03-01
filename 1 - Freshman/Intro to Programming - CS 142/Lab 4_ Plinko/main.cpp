/*
Test Cases:
 1.
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
 
 Dropping the chip into slot 5
 Path: [4.5 5.0 4.5 5.0 4.5 5.0 5.5 6.0 6.5 6.0 5.5 6.0]
 Winnings: $1000.00
 
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
 
    Choice: g
 
    INVALID:
    Please choose 1, 2, or 3
 
    MENU:
    Welcome to the Plinko Simulator!
    Please choose your operation:
    1 - Drop a single chip into one slot
    2 - Drop multiple chips into one slot
    3 - Quit the program
 
    Choice: 2
 
    MULTIPLE:
    Please input how many chips will be dropped [<0]
    Choice: 0
 
    INVALID:
    Please input a number over 0
 
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
 3 - Quit the program
 
 Choice: 2
 
 MULTIPLE:
 Please input how many chips will be dropped [<0]
 Choice: 70
 
 Please select which slot to drop your chip [0-8]
 Choice: 5
 
 Dropping 70 chips into slot 5
 Total winnings from 70 chips: $166000.00
 Average winnings per chip: $2371.00
 
 MENU:
 Welcome to the Plinko Simulator!
 Please choose your operation:
 1 - Drop a single chip into one slot
 2 - Drop multiple chips into one slot
 3 - Quit the program
 
 Choice: 3
 
 GOODBYE

*/

#include <iomanip>
#include <iostream>
#include <ctime>

using namespace std;

int main(){
    srand(time(0));
    int userChoice = 1;
    double slotChoice;
    double numChips;
    
    const int WIN_0 = 100;
    const int WIN_1 = 500;
    const int WIN_2 = 1000;
    const int WIN_3 = 0;
    const int WIN_4 = 10000;        //PRIZE MONEY VALUES^^vv
    const int WIN_5 = 0;
    const int WIN_6 = 1000;
    const int WIN_7 = 500;
    const int WIN_8 = 100;

    while ((userChoice >= 1 && userChoice < 3)){


    cout << "MENU:\nWelcome to the Plinko Simulator!\nPlease choose your operation:\n1 - Drop a single chip into one slot\n2 - Drop multiple chips into one slot\n3 - Quit the program\n\nChoice: ";                //Initial prompt
    cin >> userChoice;
        
        if(cin.fail() || userChoice<1 || userChoice >3){
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
                
                cout << fixed << setprecision(1) << position;
                if(counter<12){
                    cout << " ";
                }
            }
            cout << "]" << endl;
            
            double prizeMoney;
            
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
                double prizeSum = 0;
                int multiCounter = numChips; //
                
                for(multiCounter; multiCounter>=0; multiCounter--) {
                    
                    double position = slotChoice;
                    for(int counter=1; counter <=12; counter++){
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
                    }
                    
                    int prizeMoney;
                
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
                    prizeSum = prizeSum + prizeMoney;
                }
                cout << "Total winnings from " << numChips << " chips: $" << fixed << setprecision(2) << prizeSum << endl;      //Sum of all individual wins
                cout << "Average winnings per chip: $" << fixed << setprecision(2) << prizeSum/numChips << "\n***\n\n";              //Prize money divided by chips used
            }
        }
    }      //Drop Multiple Chips
    else if(userChoice == 3){}          //if the user input is 3, move outside of while loop
    else{userChoice = 1;}               //else, set user choice to 1, begin the loop again
    }
    cout << "\nGOODBYE\n";              //bye-bye
    
    return 0;
}
