#include "AVL.h"
#include <cmath>

using namespace std;

NodeInterface* AVL::getRootNode() const{
  return root;
}

string AVL::direction(Node* &T, char x){
  cout << "direction() = ";
  //RIGHT CHILD
  if(x == 'r'){
    if(T->rightChild->leftChild == NULL || (T->rightChild->rightChild != NULL && T->rightChild->rightChild->getHeight() >= T->rightChild->leftChild->getHeight())){
      cout << "right-right" << endl;
      return "rr"; //right-right
    }
    else{
      cout << "right-left" << endl;
      return "rl"; //right-left
    }
  }
  //LEFT CHILD
  else if(x == 'l'){
    if(T->leftChild->rightChild == NULL || (T->leftChild->leftChild != NULL && T->leftChild->leftChild->getHeight() >= T->leftChild->rightChild->getHeight())){
      cout << "left-left" << endl;
      return "ll"; //left-right
    }
    else{
      cout << "left-right" << endl;
      return "lr"; //left-left
    }
  }
}

void AVL::rotateRight(Node* &T){
  cout << "rotateRight(" << T->data << ")" << endl;
  Node* dad = T;
  Node* son = T->leftChild;
  dad->leftChild = son->rightChild;
  son->rightChild = dad;
  cout << son->data << " adopts " << dad->data << " as it's right child!\n" << dad->data << " adopts ";
  if(dad->leftChild == NULL){cout << "NULL as it's left child!" << endl;}
  else{cout << dad->leftChild->data << " as it's left child!" << endl;}
  T = son;
}

void AVL::rotateLeft(Node* &T){
  cout << "rotateLeft(" << T->data << ")" << endl;
  Node* dad = T;
  Node* son = T->rightChild;
  dad->rightChild = son->leftChild;
  son->leftChild = dad;
  cout << son->data << " adopts " << dad->data << " as it's left child!\n" << dad->data << " adopts ";
  if(dad->rightChild == NULL){cout << "NULL as it's right child!" << endl;}
  else{cout << dad->rightChild->data << " as it's right child!" << endl;}
  T = son;
}

void AVL::rebalance(Node* &T){
  cout << "rebalance()-------------------------------" << endl;
  if(T != NULL){
    cout << "Is " << T->data << " balanced?" << endl;

    //NO CHILDREN
    if(T->leftChild == NULL && T->rightChild == NULL){
      cout << "no children, balanced!" << endl;
    }

    //LEFT CHILD ONLY
    else if(T->leftChild != NULL && T->rightChild == NULL){
      cout << "LEFT CHILD ONLY: " << T->leftChild->data << endl;
      if(T->leftChild->getHeight() > 1){
        cout << "not balanced!" << endl;
        string x = direction(T, 'l');
        //LEFT RIGHT
        if(x=="lr"){
          rotateLeft(T->leftChild);
        }
        //LEFT LEFT
        rotateRight(T);
        cout << T->data << " now has two children: " << T->leftChild->data << " and " << T->rightChild->data << "!" << endl;
      }
      else{cout << "balanced!" << endl;}
    }
    //RIGHT CHILD ONLY
    else if(T->leftChild == NULL && T->rightChild != NULL){
      cout << "RIGHT CHILD ONLY: " << T->rightChild->data << endl;
      if(T->rightChild->getHeight() > 1){
        cout << "not balanced!" << endl;
        string x = direction(T, 'r');

        if(x=="rl"){rotateRight(T->rightChild);} //RIGHT LEFT
        rotateLeft(T); //RIGHT RIGHT

        cout << T->data << " now has two children: " << T->leftChild->data << " and " << T->rightChild->data << "!" << endl;
      }
      else{cout << "balanced!" << endl;}
    }

    //TWO CHILDREN
    else{
      cout << "TWO CHILDREN: " << T->leftChild->data << " and " << T->rightChild->data << endl;
      if(abs(T->leftChild->getHeight()-T->rightChild->getHeight()) > 1){
        cout << "not balanced!" << endl;
        string x = direction(T, 'r');

        //LEFT CHILD IMBALANCE
        if(T->leftChild->getHeight()>T->rightChild->getHeight()){
          if(x=="lr"){rotateLeft(T->leftChild);} //LEFT RIGHT
          rotateRight(T); //LEFT LEFT
        }

        //RIGHT CHILD IMBALANCE
        else{
          if(x=="rl"){rotateRight(T->rightChild);} //RIGHT LEFT
          rotateLeft(T); //RIGHT RIGHT
        }

        cout << T->data << " now has two children: " << T->leftChild->data << " and " << T->rightChild->data << "!" << endl;
      }
      else{cout << "balanced!" << endl;}
    }
  }
  else{cout << "NULL... does that mean it's balanced?" << endl;}
}

bool AVL::add(int data){
  cout << "add(" << data << ")------------------------------------!!!" << endl;
  if(root==NULL){
    Node * boi = new Node(data);
    root = boi;
    //cout << "tree empty, successfuly added " << data << " as root!" << endl;
    return true;
  }
  else{
    return addRec(data, root);
  }
}

bool AVL::addRec(int data, Node* &T){
  //cout << "addRec(" << data << ")" << endl;
  if(T == NULL){return false;}
  if(data > T->data){
    //cout << data << " is greater than " << T->data << endl;
    if(T->rightChild == NULL){
      Node * boi = new Node(data);
      T->rightChild = boi;
      //cout << data << " successfully added as " << T->data << "'s right child!" << endl;
      rebalance(T);
      return true;
    }
    else{
      //cout << "checking right child..." << endl;
      if(addRec(data, T->rightChild)){
        rebalance(T);
        return true;
      }
      else{return false;}
    }
  }
  else if(data < T->data){
    //cout << data << " is less than " << T->data << endl;
    if(T->leftChild == NULL){
      Node * boi = new Node(data);
      T->leftChild = boi;
      //cout << data << " successfully added as " << T->data << "'s left child!" << endl;
      rebalance(T);
      return true;
    }
    else{
      //cout << "checking left child..." << endl;
      if(addRec(data, T->leftChild)){
        rebalance(T);
        return true;
      }
      else{return false;}
    }
  }
  else if(data == T->data){
    //cout << data << " already exists on tree! Not added" << endl;
    return false;
  }
}


int AVL::getHighest(Node* T){
  if(T->rightChild == NULL){return T->data;}
  return getHighest(T->rightChild);
}

int AVL::children(Node* T){
  int n = 0;
  if(T->rightChild != NULL){n++;}
  if(T->leftChild != NULL){n++;}
  return n;
}

/*
* Attempts to remove the given int from the AVL tree
* Rebalances the tree if data is successfully removed
*
* @return true if successfully removed
* @return false if remove is unsuccessful(i.e. the int is not in the tree)
*/
bool AVL::remove(int data){
  cout << "remove(" << data << ")---------------------------------!!!" << endl;
  return sacar(root, data);
}

bool AVL::sacar(Node* &T, int data){
  //cout << "sacar(" << data << ")" << endl;
  //EMPTY TREE, OR LEAVES REACHED
  if(T == NULL){
    //cout << data << " cannot be found in the tree"<< endl;
    return false;
  }
  //GREATER THAN T
  if(T->data < data){
    //cout << "searching " << T->data << "'s right children for " << data << "..." << endl;
    if(sacar(T->rightChild, data)){
      rebalance(T);
      return true;
    }
    else{return false;}
  }
  //LESS THAN T
  else if(T->data > data){
    //cout << "searching " << T->data << "'s left children for " << data << "..." << endl;
    if(sacar(T->leftChild, data)){
      rebalance(T);
      return true;
    }
    else{return false;}  }
  //DATA EQUALS T
  else{
    //NO CHILDREN
    if(children(T) == 0){
      //cout << "deleting childless node: " << T->data << endl;
      if(T==root){
        delete root;
        root = NULL;
      }
      else{
        delete T;
        T = NULL;
      }
      rebalance(T);
      return true;
    }
    //ONE CHILD
    else if(children(T) == 1){
      //cout << T->data << " has ";
      //RIGHT
      if(T->leftChild == NULL){
        //T REPLACED WITH RIGHT CHILD
        if(T!=root){
          //cout << "a right child: ";
          Node* boi = T;
          T = boi->rightChild;
          //cout << T->data << endl;
          //cout << boi->data << " deleted, replaced with " << T->data << endl;
          delete boi;
          boi = NULL;
          rebalance(T);
          return true;
        }
        //REPLACE ROOT WITH RIGHT CHILD
        else{
          //cout << "been found at root, replaced with " << T->rightChild->data << endl;
          Node* boi = T;
          T = T->rightChild;
          delete boi;
          boi = NULL;
          rebalance(T);
          return true;
        }
      }
      //LEFT
      else{
        //T REPLACED WITH LEFT CHILD
        if(T != root){
          //cout << "a left child: ";
          Node* boi = T;
          T = boi->leftChild;
          //cout << T->data << endl;
          //cout << boi->data << " deleted, replaced with " << T->data << endl;
          delete boi;
          boi = NULL;
          rebalance(T);
          return true;
        }
        //ROOT REPLACED WITH LEFT CHILD
        else{
          //cout << " been found at root, replaced with " << T->leftChild->data << endl;
          Node* boi = T;
          T = T->leftChild;
          delete boi;
          boi = NULL;
          rebalance(T);
          return true;
        }
      }
    }
    //TWO CHILDREN
    else{
      //cout << "two children: " << T->leftChild->data << " and " << T->rightChild->data << endl;
      int n = getHighest(T->leftChild);
      sacar(T->leftChild, n);
      //cout << T->data << " replaced by " << n << endl;
      T->data = n;
      rebalance(T);
      return true;
    }
  }
}

void AVL::clear(){
  cout << "clear()--------------------------------------------------" << endl;
  while(root != NULL){
    remove(root->data);
  }
}
