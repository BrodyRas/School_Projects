#include "BSTInterface.h"
#include "Node.h"

class BST : public BSTInterface {
public:
	BST() {root = NULL;}
	~BST() {clear();}

	//Please note that the class that implements this interface must be made
	//of objects which implement the NodeInterface

	NodeInterface* getRootNode() const;
	bool add(int data);
	bool addRec(int data, Node* &T);
	int getHighest(Node* T);
	int children(Node* T);
	bool remove(int data);
	bool sacar(Node* &T, int data);
	void clear();

private:
  Node* root;
};
