#include "LinkedListInterface.h"
#include <sstream>

template <class T>
class LinkedList:public LinkedListInterface <T> {

public:
	LinkedList() {
		head = NULL;
	};
	~LinkedList() {clear();};

	bool find(T value){
		cout << "find(" << value << ")" << endl;
		if(head==NULL){
			cout << "empty" << endl;
			return false;
		}
		Node *current = head;
		while(current!=NULL){
			if(current->value==value){
				cout << value << " found!" << endl;
				return true;
			}
			current = current->next;
		}
		cout << value << " not found!" << endl;
		return false;
	}

	void insertHead(T value){
		cout << "insertHead(" << value << ")" << endl;
		if(!find(value)){
			if(head==NULL){
				Node *boi = new Node();
				boi->value = value;
				boi->next = NULL;
				head = boi;
				cout << value << " inserted at head" << endl;
			}
			else{
				Node *boi = new Node();
				boi->value = value;
				boi->next = head;
				head = boi;
				cout << value << " inserted at head" << endl;
			}
		}
	}

	void insertTail(T value){
		cout << "insertTail(" << value << ")" << endl;
		if(head==NULL){insertHead(value);}
		else if(!find(value)){
				Node *boi = new Node();
				boi->value = value;
				boi->next = NULL;
				Node *temp = head;
				while(temp->next!=NULL){
					temp = temp->next;
				}
				temp->next = boi;
		}
	}

	void insertAfter(T value, T insertionNode){
		if(!find(value)){
			cout << "insertAfter(" << value << ", " << insertionNode << ")" << endl;
			if(find(insertionNode)){
				Node *current = head;
				while(current->value!=insertionNode){
					current = current->next;
				}
				cout << "inserting " << value << " after " << insertionNode << endl;
				Node *next = new Node();
				next->value = value;
				next->next = current->next;
				current->next = next;
			}
		}
	}

	void remove(T value){
		cout << "remove(" << value << ")" << endl;
		if(head!=NULL && find(value)){
			Node *current = head;
			if(head->value==value){
				cout << value << " found at head, removed" << endl;
				current = head->next;
				delete head;
				head = current;
			}
			else{
				cout << "non-head deletion" << endl;
				while(current->next->value!=value){
					current = current->next;
				}
				Node *temp = current->next;
				current->next = temp->next;
				delete temp;
				temp = NULL;
			}
		}
  }

	void clear(){
		cout << "clear()------------------------------" << endl;
		if(head!=NULL){
			Node *current = head;
			Node *after = head->next;

			while(current != NULL && after != NULL){
				delete current; //delete the data that current is pointing to
				current = after;
				after = after->next;
			}
		delete current;
		head = NULL;
		}
		cout << "clear() successful" << endl;
  }

	T at(int index){
		cout << "at(" << index << ") = ";
		if(index<0 || head==NULL){
			cout << "out_of_range #1" << endl;
			throw out_of_range("out_of_range");
		}
		Node *current = head;
		for(int i=0; i<index; i++){
			if(current->next==NULL){
				cout << "out_of_range #2" << endl;
				throw out_of_range("out_of_range");
			}
			current = current->next;
		}
		cout << current->value << endl;
		return current->value;
	}

	int size(){
		cout << "size() = ";
		if(head==NULL){
			cout << 0 << endl;
			return 0;
		}
		Node *current = head;
		int i = 1;
		while(current->next!=NULL){
			i++;
			current = current->next;
		}
		cout << i << endl;
		return i;
	}

	string toString(){
		cout << "toString()" << endl;
		if(head==NULL){
			cout << "NULL" << endl;
			return "";
		}
		Node *current = head;
		Node *next = head->next;
		stringstream ss;
		while(current != NULL){
				ss << current->value;
				if(next!=NULL){
					ss << " ";
					next = next->next;
				}
				current = current->next;
		}
		cout << ss.str() << endl;
		return ss.str();
  }

private:
		struct Node {
			T value;
			Node* next;
		};
		Node* head;
};
