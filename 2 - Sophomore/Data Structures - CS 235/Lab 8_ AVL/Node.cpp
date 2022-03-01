#include "Node.h"

int Node::getData() const{
  return data;
}

NodeInterface* Node::getLeftChild() const{
  return leftChild;
}

NodeInterface* Node::getRightChild() const{
  return rightChild;
}

/*
* Returns the height of this node. The height is the number of nodes
* along the longest path from this node to a leaf.  While a conventional
* interface only gives information on the functionality of a class and does
* not comment on how a class should be implemented, this function has been
* provided to point you in the right direction for your solution.  For an
* example on height, see page 448 of the text book.
*
* @return the height of this tree with this node as the local root.
*/
int Node::getHeight(){
  if(leftChild==NULL&&rightChild==NULL){return 1;}
  else if(leftChild==NULL){return rightChild->getHeight()+1;}
  else if(rightChild==NULL){return leftChild->getHeight()+1;}
  else{return max(rightChild->getHeight(), leftChild->getHeight())+1;}
}
