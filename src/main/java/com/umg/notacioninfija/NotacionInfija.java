/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.umg.notacioninfija;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author johan
 */
public class NotacionInfija {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Escribe una operación: ");
    String operationUser = scanner.nextLine();
    scanner.close();
    String cleanOperation = operationUser.replaceAll("\\s", "");

    ArrayList<String> operation = parseOperation(cleanOperation);
    ArrayList<String> result = infixNotationToPostfixNotation(operation);
    System.out.println("Notación Infija a PostFija");
    for (String element : result) {
      System.out.print(element + ",");
    }
    System.out.println("\nResultado: ");
    System.out.println(operatePostfixNotation(result));
  }

  public static int operatePostfixNotation(ArrayList<String> postfixOperation) {
    int firstNumber = 0;
    int secondNumber = 0;
    int number = 0;
    Stack<Integer> stackOperateNumbers = new Stack<Integer>();
    for (String element : postfixOperation) {
      if (!isNumber(element)) {
        firstNumber = stackOperateNumbers.pop();
        secondNumber = stackOperateNumbers.pop();
        switch (element) {
          case "+" -> stackOperateNumbers.add(secondNumber + firstNumber);
          case "-" -> stackOperateNumbers.add(secondNumber - firstNumber);
          case "*" -> stackOperateNumbers.add(secondNumber * firstNumber);
          case "/" -> stackOperateNumbers.add(secondNumber / firstNumber);
          case "^" ->
              stackOperateNumbers.add((int) Math.pow((double) secondNumber, (double) firstNumber));
        }
        ;
      } else if (isNumber(element)) {
        number = Integer.parseInt(element);
        stackOperateNumbers.add(number);
      }
    }
    return stackOperateNumbers.pop();
  }

  public static ArrayList<String> infixNotationToPostfixNotation(ArrayList<String> operation) {
    ArrayList<String> result = new ArrayList<String>();
    String swapOperator;
    Stack<String> infixStack = new Stack<String>();

    for (String currentElement : operation) {

      // Introducir numeros al resultado
      if (isNumber(currentElement)) {
        result.add(currentElement);
      }
      // Igual Jerarquia Cambiar
      else if (!infixStack.isEmpty()
          && !isNumber(currentElement)
          && !currentElement.equals("(")
          && checkHierachy(currentElement) == checkHierachy(infixStack.peek())) {
        swapOperator = infixStack.pop();
        result.add(swapOperator);
        infixStack.add(currentElement);
      }
      // Mayor Jerarquia Añadir a la Pila
      else if (!infixStack.isEmpty()
          && !isNumber(currentElement)
          && !currentElement.equals("(")
          && checkHierachy(currentElement) > checkHierachy(infixStack.peek())) {
        infixStack.add(currentElement);
      }
      // Menor Jerarquia Sacar de la Pila
      else if (!infixStack.isEmpty()
          && !isNumber(currentElement)
          && !currentElement.equals("(")
          && !currentElement.equals(")")
          && checkHierachy(currentElement) < checkHierachy(infixStack.peek())) {
        while (!infixStack.isEmpty()
            && checkHierachy(currentElement) < checkHierachy(infixStack.peek())
            && !infixStack.peek().equals("(")) {
          result.add(infixStack.pop());
        }
        infixStack.add(currentElement);
      }
      // Introducir Operadores a la pila al inicio
      else if ((!isNumber(currentElement) && !currentElement.equals(")") && infixStack.isEmpty())
          || currentElement.equals("(")) {
        infixStack.add(currentElement);
      }
      // Si se encuentra un ')', sacar todo hasta encontrar un '('
      else if (!infixStack.isEmpty() && currentElement.equals(")")) {
        while (!infixStack.peek().equals("(")) {
          result.add(infixStack.pop());
        }
        infixStack.pop();
      }
    }

    // Añadir al resultado todo lo sobrante
    while (!infixStack.isEmpty()) {
      result.add(infixStack.pop());
    }

    return result;
  }

  public static int checkHierachy(String operator) {
    return switch (operator) {
      case "+" -> 1;
      case "-" -> 1;
      case "*" -> 2;
      case "/" -> 2;
      case "^" -> 3;
      default -> -1;
    };
  }

  public static boolean isOperator(String operator) {
    return switch (operator) {
      case "+" -> true;
      case "-" -> true;
      case "*" -> true;
      case "/" -> true;
      case "^" -> true;
      default -> false;
    };
  }

  public static ArrayList<String> parseOperation(String operation) {
    ArrayList<String> parsedOperation = new ArrayList<String>();
    Pattern pattern = Pattern.compile("\\d+|[-+*/^()]");
    Matcher matcher = pattern.matcher(operation);

    while (matcher.find()) {
      parsedOperation.add(matcher.group());
    }

    return parsedOperation;
  }

  public static boolean isNumber(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
