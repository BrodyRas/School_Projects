#include "BST.h"

/*
* Returns the root node for this tree
*
* @return the root node for this tree.
*/
NodeInterface* BST::getRootNode() const{
  return root;
}

/*
* Attempts to add the given int to the BST tree
*
* @return true if added
* @return false if unsuccessful (i.e. the int is already in tree)
*/
bool BST::add(int data){
  cout << "add(" << data << ")--------------------------------" << endl;
  if(root==NULL){
    Node * boi = new Node(data);
    root = boi;
    cout << "tree empty, successfuly added " << data << " as root!" << endl;
    return true;
  }
  else{
    return addRec(data, root);
  }
}

bool BST::addRec(int data, Node* &T){
  cout << "addRec(" << data << ")" << endl;
  if(T == NULL){return false;}
  if(data>T->data){
    cout << data << " is greater than " << T->data << endl;
    if(T->rightChild == NULL){
      Node * boi = new Node(data);
      T->rightChild = boi;
      cout << data << " successfully added as " << T->data << "'s right child!" << endl;
      return true;
    }
    else{
      cout << "checking right child..." << endl;
      return addRec(data, T->rightChild);
    }
  }
  else if(data<T->data){
    cout << data << " is less than " << T->data << endl;
    if(T->leftChild == NULL){
      Node * boi = new Node(data);
      T->leftChild = boi;
      cout << data << " successfully added as " << T->data << "'s left child!" << endl;
      return true;
    }
    else{
      cout << "checking left child..." << endl;
      return addRec(data, T->leftChild);
    }
  }
  else if(data==T->data){
    cout << data << " already exists on tree! Not added" << endl;
    return false;
  }
}

int BST::getHighest(Node* T){
  if(T->rightChild==NULL){return T->data;}
  return getHighest(T->rightChild);
}

int BST::children(Node* T){
  int n = 0;
  if(T->rightChild!=NULL){n++;}
  if(T->leftChild!=NULL){n++;}
  return n;
}

bool BST::remove(int data){
  cout << "remove(" << data << ")----------------------------" << endl;
  return sacar(root, data);
}

bool BST::sacar(Node* &T, int data){
  cout << "sacar(" << data << ")" << endl;
  //EMPTY TREE, OR LEAVES REACHED
  if(T==NULL){
    cout << data << " cannot be found in the tree"<< endl;
    return false;
  }
  //GREATER THAN T->DATA
  if(T->data < data){
    cout << "searching " << T->data << "'s right children for " << data << "..." << endl;
    return sacar(T->rightChild, data);
  }
  //LESS THAN T->DATA
  else if(T->data > data){
    cout << "searching " << T->data << "'s left children for " << data << "..." << endl;
    return sacar(T->leftChild, data);
  }
  //DATA EQUALS T->DATA
  else{
    //NO CHILDREN
    if(children(T)==0){
      cout << "deleting childless node: " << T->data << endl;
      if(T==root){
        delete root;
        root = NULL;
      }
      else{
        delete T;
        T = NULL;
      }
      return true;
    }
    //ONE CHILD
    else if(children(T)==1){
      cout << T->data << " has ";
      //RIGHT
      if(T->leftChild==NULL){
        //T REPLACED WITH RIGHT CHILD
        if(T!=root){
          cout << "a right child: ";
          Node* boi = T;
          T = boi->rightChild;
          cout << T->data << endl;
          cout << boi->data << " deleted, replaced with " << T->data << endl;
          delete boi;
          boi = NULL;
          return true;
        }
        //REPLACE ROOT WITH RIGHT CHILD
        else{
          cout << "been found at root, replaced with " << T->rightChild->data << endl;
          Node* boi = T;
          T = T->rightChild;
          delete boi;
          boi = NULL;
          return true;
        }
      }
      //LEFT
      else{
        //T REPLACED WITH LEFT CHILD
        if(T!=root){
          cout << "a left child: ";
          Node* boi = T;
          T = boi->leftChild;
          cout << T->data << endl;
          cout << boi->data << " deleted, replaced with " << T->data << endl;
          delete boi;
          boi = NULL;
          return true;
        }
        //ROOT REPLACED WITH LEFT CHILD
        else{
          cout << " been found at root, replaced with " << T->leftChild->data << endl;
          Node* boi = T;
          T = T->leftChild;
          delete boi;
          boi = NULL;
          return true;
        }
      }
    }
    //TWO CHILDREN
    else{
      cout << "two children: " << T->leftChild->data << " and " << T->rightChild->data << endl;
      int n = getHighest(T->leftChild);
      sacar(T->leftChild, n);
      cout << T->data << " replaced by " << n << endl;
      T->data = n;
      return true;
    }
  }
}

void BST::clear(){
  cout << "clear()--------------------------------------------------" << endl;
  while(root!=NULL){
    remove(root->data);
  }
}
