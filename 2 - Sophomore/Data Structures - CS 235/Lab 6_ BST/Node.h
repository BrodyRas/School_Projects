#include "NodeInterface.h"

class Node : public NodeInterface {
friend class BST;
public:
	Node(int x) {
    data = x;
		leftChild = NULL;
		rightChild = NULL;
  }
	~Node() {}

	int getData() const;
	NodeInterface* getLeftChild() const;
	NodeInterface* getRightChild() const;

private:
  int data;
  Node* leftChild;
  Node* rightChild;
};
