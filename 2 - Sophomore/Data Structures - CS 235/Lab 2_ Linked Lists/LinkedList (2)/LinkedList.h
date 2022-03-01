#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include <stdio.h>
#include <iostream>
#include <string>

using namespace std;

template <class ItemType>
class LinkedList {
public:

    class Node {
    public:
        ItemType value;
        Node *next;

        Node(ItemType valor) {
            value = valor;
            next = NULL;
        }
    };

    LinkedList() {
        head = tail = NULL;
        temano = 0;
    }

    void insert(int index, ItemType val) {
        if ((index > temano) || (index < 0)) {
            cout << "Can't add this." << endl;
            return;
        }

        if (index == temano) {
            append(val);
        }
        else if (index == 0) {
            Node *n = new Node(val);
            n->next = head;
            head = n;
        }
        else {
            Node *n = new Node(val);
            Node *prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev->next;
            }
            n->next = prev->next;
            prev->next = n;
        }

        temano ++;
    }

    void remove(int index) {
        if ((index >= temano) || (index < 0)) {
            cout << "Can't remove that dobrain." << endl;
            return;
        }

        if (index == 0) {  // we are removing the head
            Node *cur = head;
            head = head->next;
            delete cur;
            if (temano == 1)
                tail = NULL;
        }
        else if (index == (temano-1)) { // we are removing the tail
            Node *cur = head;
            while (cur->next != tail) {
                cur = cur->next;
            }
            delete tail;
            cur->next = NULL;
            tail = cur;
        }
        else {  // we are removing something in the middle
            Node *prev = head;
            for (int i = 0; i < index-1; i++) {
                prev = prev->next;
            }
            Node *cur = prev->next;
            prev->next = cur->next;
            delete cur;
        }

        temano --;
    }

    void append(ItemType newVal) {
        if (temano == 0) {
            head = tail = new Node(newVal);
            temano ++;
            return;
        }
    
        tail->next = new Node(newVal);
        tail = tail->next;
        temano ++;
    }

    ItemType get(int index) {
        if (index >= temano) {
            cout << "This is gonna crash." << endl;
            return NULL;
        }

        Node *cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur->next;
        }

        return cur->value;
    }

    void set(int index, ItemType val) {
        if (index >= temano) {
            cout << "You blessed soul." << endl;
            return;
        }

        Node *cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur->next;
        }

        cur->value = val;
    }

    int size() {
        return temano;
    }

    void print() {
        cout << "Print list of length: " << temano << endl;
        Node *current = head;

        while (current != NULL) {
            cout << current->value << " ";
            current = current->next;
        }
        cout << endl;
    }

private:
    Node *head, *tail;
    int temano;

};


#endif
