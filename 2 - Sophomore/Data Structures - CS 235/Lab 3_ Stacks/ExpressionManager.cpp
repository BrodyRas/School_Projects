#include <iostream>
#include <string>
#include <stack>
#include <sstream>
#include <cctype>
#include "ExpressionManager.h"

using namespace std;

//checks for non-digits, non-operators, and non-parentheses
bool ExpressionManager::isClean(string expression){

  for(int i=0; i < expression.size(); i++){
    char x = expression[i];
    if(!isdigit(x) && x != '(' && x != ')' && x != '[' && x != ']' && x != '{' && x != '}' && x != ' ' && x != '+' && x != '-' && x != '*' && x != '/' && x != '%'){
      return false;
    }
  }

  return true;
}

//assures that the parsed parentheses compliments the top of the stack
bool ExpressionManager::checkParentheses(char c, stack<char> &check){

  if(c == ')'){
    if(check.top() == '('){
      check.pop();
      return true;
    }
    else{return false;}
  }
  else if(c == ']'){
    if(check.top() == '['){
      check.pop();
      return true;
    }
    else{return false;}
  }
  else if(c == '}'){
    if(check.top() == '{'){
      check.pop();
      return true;
    }
    else{return false;}
  }
}

//checks whether an expression has balanced parentheses
bool ExpressionManager::isBalanced(string expression){
  bool clean = isClean(expression);
  if(clean == false){return false;}

  stack<char> check;

  for(int i = 0; i<expression.size(); i++){
    if(!isdigit(expression[i]) || expression[i] != ' '){
      if(expression[i] == '(' || expression[i] == '[' || expression[i] == '{'){
        check.push(expression[i]);
      }
      if(expression[i] == ')' || expression[i] == ']' || expression[i] == '}'){
        if(check.empty()){return false;}
        if(checkParentheses(expression[i], check) == false){return false;}
      }
    }
  }
  if(check.empty()){
  return true;
  }
  else{return false;}
}

//converts postfix expressions to infix expressions
string ExpressionManager::postfixToInfix(string postfixExpression){

  if(!isClean(postfixExpression) || !isBalanced(postfixExpression)){return "invalid";}

  if(postfixExpression.size()<=1){
    return "invalid";
  }

  stringstream ss;
  stack<string> son;

  ss << postfixExpression;
  string dude;

  while(ss >> dude){
        //adds digits to stringstream
        if(isdigit(dude[0])){
          son.push(dude);
        }
        else{
          if(son.size()<2){return "invalid";}
          string x = son.top();
          son.pop();
          son.top() = "( " + son.top() + " " + dude[0] + " " + x + " )";
        }
  }
    string answer = son.top();

    if(son.size()>1){
      return "invalid";
    }
    else{return answer;}
}

//evaluates postfix expressions
string ExpressionManager::postfixEvaluate(string postfixExpression){

  if(!isClean(postfixExpression) || !isBalanced(postfixExpression)){return "invalid";}

  if(postfixExpression.size()<1){
    return "invalid";
  }

  stringstream ss;
  stack<int> son;

  ss << postfixExpression;
  string dude;

  while(ss >> dude){
      //adds digits to stringstream
      if(isdigit(dude[0])){
        stringstream ss2;
        ss2 << dude; //picks up string
        int x;
        ss2 >> x; //converts string to int
        son.push(x);
      }
      //addition
      else if(dude[0] == '+'){
        if(son.size()<2){return "invalid";}
        int x = son.top();
        son.pop();
        son.top() += x;
      }
      //subtraction
      else if(dude[0] == '-'){
        if(son.size()<2){return "invalid";}
        int x = son.top();
        son.pop();
        son.top() -= x;
      }
      //multiplication
      else if(dude[0] == '*'){
        if(son.size()<2){return "invalid";}
        int x = son.top();
        son.pop();
        son.top() *= x;
      }
      //division
      else if(dude[0] == '/'){
        if(son.size()<2){return "invalid";}
        if(son.top()==0){return "invalid";} //prevents division by 0
        int x = son.top();
        son.pop();
        son.top() /= x;
      }
      //modulo
      else if(dude[0] == '%'){
        if(son.size()<2){return "invalid";}
        if(son.top()==0){return "invalid";} //prevents division by 0
        int x = son.top();
        son.pop();
        son.top() %= x;
      }
    }

  string answer = to_string(son.top()); //converts int on top of stack to returnable string

  if(son.size()>1){
    return "invalid";
  }
  else{return answer;}
}

//returns precedence of character
int ExpressionManager::precedence(char c){
  if(c == ')' || c == ']' || c == '}'){
    return 3;
  }
  else if(c == '*' || c == '/' || c == '%'){
    return 2;
  }
  else if(c == '+' || c == '-'){
    return 1;
  }
  else if(c == '(' || c == '[' || c == '{'){
    return 0;
  }
}

//tells which path to take based on precedence of passed char, and precedence of master.top()
bool ExpressionManager::eval(char c, stack<char> &master, string &answer){

  if(precedence(c) == 3){
    while(precedence(master.top()) != 0){
      answer.push_back(master.top());
      answer.push_back(' ');
      master.pop();
    }
    master.pop();
    return true;
  }
  else if(master.empty() || precedence(c) > precedence(master.top()) || precedence(c) == 0){
    master.push(c);
    return true;
  }
  else if(precedence(c) <= precedence(master.top())){
    while(!master.empty() && precedence(c) <= precedence(master.top())){
      answer.push_back(master.top());
      answer.push_back(' ');
      master.pop();
    }
    master.push(c);
    return true;
  }
}

//converts infix expressions to postfix expressions
string ExpressionManager::infixToPostfix(string infixExpression){
  if(!isClean(infixExpression) || !isBalanced(infixExpression)){return "invalid";}
  stringstream ss;
  string dude;
  string answer;
  stack<char> master;

  ss << infixExpression;

  //every valid infix expression begins either with a parentheses, or a digit
  if(!isdigit(infixExpression[0]) && infixExpression[0] != '(' && infixExpression[0] != '[' && infixExpression[0] != '{'){
    return "invalid";
  }

  while(ss >> dude){
    //immediately pushes integers onto string
    if(isdigit(dude[0])){
      answer += dude + " ";
    }
    //evaluates precedence of char to master.top(), decides what to do
    else if(eval(dude[0], master, answer) == false){
      return "invalid";
    }
  }

  int x = master.size();
  //empty any remaining chars into string
  for(int i = 0; i < x; i++){
    answer += master.top();
    if(i!=x-1){answer += ' ';}
    master.pop();
  }

  //makes sure it's a legit expression
  if(postfixEvaluate(answer) == "invalid"){return "invalid";}

  //eliminates hanging space
  if(answer.back() == ' '){answer.pop_back();}

  //I win
  return answer;
}
