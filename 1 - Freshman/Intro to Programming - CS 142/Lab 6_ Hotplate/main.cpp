#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <iomanip>

using namespace std;

const int FIRST_ROW = 0;
const int LAST_ROW = 19;
const int ARRAY_SIZE = 20;
const int INTERNAL_SIZE = 18 * 18;
void initialize_hot_plate(double array[][20], int ARRAY_SIZE)
{
    
    for (int row = 0; row < ARRAY_SIZE; row++)
    {
        for (int column = 0; column < ARRAY_SIZE; column++)
        {
            array[row][column] = 0;
        }
    }//initialize all values to 0
    
    for (int column = 1; column < ARRAY_SIZE - 1; column++)
    {
        array[FIRST_ROW][column] = 100;
    }//set first row to 100
    
    for (int column = 1; column < ARRAY_SIZE - 1; column++)
    {
        array[LAST_ROW][column] = 100;
    }//set last row to 100
}//Initialize array to 0, then set top and bottom to 100

void print(double array[][20]){
    
    for (int row = 0; row < ARRAY_SIZE; row++){
        for (int col = 0; col < ARRAY_SIZE; col++){
            cout << array[row][col];
            if (!(row == ARRAY_SIZE - 1 && col == ARRAY_SIZE - 1)){
                cout << ",";
            }
        }
        cout << endl;
    }
}

void update(double change_array[][20], double r_array[][20]){
    for (int row = 1; row < ARRAY_SIZE-1; row++){
        for (int col = 1; col < ARRAY_SIZE-1; col++){
            change_array[row][col] = (r_array[row + 1][col] + r_array[row - 1][col] + r_array[row][col + 1] + r_array[row][col - 1]) / 4;
        }
    }
}

void toFile(double array[][20]){
    ofstream out;
    out.open("/Users/Brody/Desktop/lab6output.csv");
    
    for (int row = 0; row < ARRAY_SIZE; row++){
        for (int col = 0; col < ARRAY_SIZE; col++){
            out << array[row][col];
            if (!(row == ARRAY_SIZE - 1 && col == ARRAY_SIZE - 1)){
                out << ",";
            }
        }
        out << endl;
    }
    
    out.close();
}

int main()
{
    double Hot1[20][20];
    double Hot2[20][20];
    
    const double POSITIVE_THRESH = .1;
    const double NEGATIVE_THRESH = -.1;
    
    
    initialize_hot_plate(Hot1, ARRAY_SIZE);
    initialize_hot_plate(Hot2, ARRAY_SIZE);
    
    print(Hot1);
    cout << endl << endl;
    
    update(Hot1, Hot2);
    
    print(Hot1);
    
    int stable = 0;
    int count = 0;
    
    while(stable < INTERNAL_SIZE){
        stable = 0;
        
        if (count % 2 == 1){
            update(Hot2, Hot1);
        }
        else{ update(Hot1, Hot2); }
        
        count++;
        
        for (int row = 1; row < ARRAY_SIZE-1; row++){
            for (int col = 1; col < ARRAY_SIZE-1; col++){
                if(Hot1[row][col]-Hot2[row][col]<POSITIVE_THRESH
                   &&Hot1[row][col]-Hot2[row][col]>NEGATIVE_THRESH){
                    stable++;
                }
            }
        }
    }
    
    toFile(Hot1);
    
    return 0;
}