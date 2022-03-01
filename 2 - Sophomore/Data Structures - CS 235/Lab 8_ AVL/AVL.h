#pragma once
#include "AVLInterface.h"
#include "Node.h"
#include <iostream>
#include <string>

using namespace std;

class AVL : public AVLInterface {
public:
	AVL() {
    root = NULL;
  }
	virtual ~AVL() {clear();}

  NodeInterface* getRootNode() const;
	string direction(Node* &T, char x);
	void rotateRight(Node* &T);
	void rotateLeft(Node* &T);
	void rebalance(Node* &T);
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
