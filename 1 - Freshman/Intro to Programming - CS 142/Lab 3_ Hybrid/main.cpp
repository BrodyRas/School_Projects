/*
 Test Cases:
 
 1:
 Miles in a year: 4000
 Price of gas: 5
 Cost of Hybrid: 5000
 Hybrid efficiency: 30
 Hybrid Resale: 3000
 Standard Cost: 4500
 Standard efficiency: 25
 Standard Resale: 3500
 Preference: Total
 
 Standard:
 Gas consumption: 800 gallons
 Total cost: 5000
 
 Hybrid:
 Gas consumption: 666.66 gallons
 Total cost: 5333.33
 
 
    2:
    Miles in a year: 50000
    Price of gas: 6
    Cost of Hybrid: 9000
    Hybrid efficiency: 50
    Hybrid Resale: 8000
    Standard Cost: 7500
    Standard efficiency: 25
    Standard Resale: 5000
    Preference: Gas
 
    Hybrid:
    Gas consumption: 5000 gallons
    Total cost: 31000
 
    Standard:
    Gas consumption: 10000 gallons
    Total cost: 62500
 
 
        3:
        Miles in a year: 25000
        Price of gas: 3
        Cost of Hybrid: 6500
        Hybrid efficiency: 50
        Hybrid Resale: 6000
        Standard Cost: 6000
        Standard efficiency: 35
        Standard Resale: 5500
        Preference: Total
 
        Hybrid:
        Gas consumption: 3125 gallons
        Total cost: 9875
 
        Standard:
        Gas consumption: 3571.43 gallons
        Total cost: 11214.3
*/

#include <iostream>
#include <string>

using namespace std;

int main(){
    const int FIVE_YEARS = 5;
    
    int yearMile;       //predicted miles per year
    float gasPrice5Year;//predicted gas price
    float hybridCost;   //initial cost of Hybrid
    float hybridGasEff; //Hybrid efficiency
    float hybridResale; //predicted resale value of hybrid
    float standCost;    //initial cost of Standard
    float standGasEff;  //Standard efficiency
    float standResale;  //predicted resale value of Standard
    
    string preference;
    
    cout << "Hybrid/Standard Price Test:\n\nHow many miles would you predict you drive in a year?\nMiles:";
    cin >> yearMile;            //Miles in a year
    
    while(yearMile<=0){
        cout << "That is not a valid value. Please enter a positive number:\nMiles:";
        cin >> yearMile;        //Miles in a year must be positive
    }
    
    
   
    cout << "What do you predict the price of a gallon of gas will be during your 5 years of ownership?\n$";
    cin >> gasPrice5Year;       //Price of gas
    
    while(gasPrice5Year<=0){
        cout << "That is not a valid value. Please enter a positive number:\n$";
        cin >> gasPrice5Year;   //Price of gas must be positive
    }
    
    
    cout << "What is the cost of the prospective hybrid?\n$";
    cin >> hybridCost;          //Cost of hybrid
    
    while(hybridCost<=0){
        cout << "That is not a valid value. Please enter a positive number:\n$";
        cin >> hybridCost;      //Cost of hybrid must be positive
    }
    
    
    cout << "How gas efficient is this hybrid, miles to the gallon?\nMPG:";
    cin >> hybridGasEff;        //Hybrid gas efficiency
    
    while(hybridGasEff<=0){
        cout << "That is not a valid value. Please enter a positive number:\nMPG:";
        cin >> hybridGasEff;    //Hybrid gas efficiency must be positive
    }
    
    
    cout << "What is the predicted resale value of this hybrid in 5 years?\n$";
    cin >> hybridResale;        //Hybrid resale
    
    while(hybridResale<=0){
        cout << "That is not a valid value. Please enter a positive number:\n$";
        cin >> hybridResale;    //Hybrid resale must be positive
    }
    
    
    cout << "What is the cost of the prospective non-hybrid?\n$";
    cin >> standCost;           //Standard cost
    
    while(standCost<=0){
        cout << "That is not a valid value. Please enter a positive number:\n$";
        cin >> standCost;       //Standard cost must be positive

    }
    
    
    cout << "How gas efficient is this non-hybrid, miles to the gallon?\nMPG:";
    cin >> standGasEff;         //Standard efficiency
    
    while(standGasEff<=0){
        cout << "That is not a valid value. Please enter a positive number:\nMPG:";
        cin >> standGasEff;     //Standard efficiency must be positive
    }
    
   
    cout << "What is the predicted resale value of this non-hybrid in 5 years?\n$";
    cin >> standResale;         //Standard resale
    
    while(standResale<=0){
        cout << "That is not a valid value. Please enter a positive number:\n$";
        cin >> standResale;     //Standard resale must be positive
    }
    
    cout << "Which would you prefer: Minimal gas consupmtion, or minimal total cost?\nGas or Total?: ";
    cin >> preference;
    
    
    //CALCULATIONS
    
    float hybridGasMileage = (yearMile / hybridGasEff)*FIVE_YEARS;
    
    float standardGasMileage = (yearMile / standGasEff)*FIVE_YEARS;
    
    float hybridTotalCost = (((yearMile / hybridGasEff)*gasPrice5Year)*FIVE_YEARS) + (hybridCost-hybridResale);
    
    float standardTotalCost = (((yearMile / standGasEff)*gasPrice5Year)*FIVE_YEARS) + (standCost-standResale);
    
    
    if(preference == "Gas" || preference == "gas"){
        
         if(hybridGasMileage < standardGasMileage){
                cout << "\nHybrid:\n";
                cout << "Gas consumption over 5 years: " << hybridGasMileage << " gallons"<< endl;
                cout << "Total cost of ownership after 5 years: $" << hybridTotalCost << endl;
         
                cout << "\nStandard:\n";
                cout << "Gas consumption over 5 years: " << standardGasMileage << " gallons" << endl;
                cout << "Total cost of ownership after 5 years: $" << standardTotalCost << endl;
            }
            else{
            
                cout << "\nStandard:\n";
                cout << "Gas consumption over 5 years: " << standardGasMileage << " gallons" << endl;
                cout << "Total cost of ownership after 5 years: $" << standardTotalCost << endl;
                
                cout << "\nHybrid:\n";
                cout << "Gas consumption over 5 years: " << hybridGasMileage << " gallons" << endl;
                cout << "Total cost of ownership after 5 years: $" << hybridTotalCost << endl;
            }
    }
    
    
    else if (preference == "Total" || preference == "total"){
        
         if(hybridTotalCost < standardTotalCost){
                cout << "\nHybrid:\n";
                cout << "Gas consumption over 5 years: " << hybridGasMileage << " gallons" << endl;
                cout << "Total cost of ownership after 5 years: $" << hybridTotalCost << endl;
         
                cout << "\nStandard:\n";
                cout << "Gas consumption over 5 years: " << standardGasMileage << " gallons" << endl;
                cout << "Total cost of ownership after 5 years: $" << standardTotalCost << endl;
         }
            else{
         
                cout << "\nStandard:\n";
                cout << "Gas consumption over 5 years: " << standardGasMileage << " gallons" << endl;
                cout << "Total cost of ownership after 5 years: $" << standardTotalCost << endl;
         
                cout << "\nHybrid:\n";
                cout << "Gas consumption over 5 years: " << hybridGasMileage << " gallons" << endl;
                cout << "Total cost of ownership after 5 years: $" << hybridTotalCost << endl;
         }
         
    }
    
    
    return 0;
}