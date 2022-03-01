#include <sstream>
#include "QSInterface.h"

class QS : public QSInterface
{
public:
	QS() {
		theArray = NULL;
		arraySize = 0;
		index = 0;
		izquierda = 0;
		derecha = -1;
	}
	virtual ~QS() {
		delete [] theArray;
	}

	void sortAll();
	int medianOfThree(int left, int right);
	int partition(int left, int right, int pivotIndex);
	string getArray() const;
	int getSize() const;
	bool addToArray(int value);
	bool createArray(int capacity);
	void clear();

private:
	int* theArray;
	int arraySize;	//maximum size
	int index;			//current size - 1, index of latest addition
	int izquierda;
	int derecha;
};
