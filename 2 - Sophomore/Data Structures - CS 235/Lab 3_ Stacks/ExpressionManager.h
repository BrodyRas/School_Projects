#pragma once
#include <iostream>
#include <string>
#include <stack>
#include "ExpressionManagerInterface.h"

using namespace std;

class ExpressionManager:public ExpressionManagerInterface{
public:
	ExpressionManager() {}
	virtual ~ExpressionManager() {}
	
  bool isClean(string expression);
	bool checkParentheses(char c, stack<char> &check);
	bool isBalanced(string expression);

	string postfixToInfix(string postfixExpression);

	string postfixEvaluate(string postfixExpression);

	int precedence(char c);
	bool eval(char c, stack<char> &master, string &answer);
	string infixToPostfix(string infixExpression);
};
