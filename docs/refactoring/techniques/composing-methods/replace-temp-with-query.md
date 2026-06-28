# Replace Temp with Query

**URL:** https://refactoring.guru/replace-temp-with-query

## Problem
You place the result of an expression in a local variable for later use in your code.

## Solution
Move the entire expression to a separate method and return the result from it. Query the method instead of using a variable.

## Why Refactor
- **Accessibility:** Local variables are only visible within the method they are in. By turning them into a "query" method, the information becomes available to other methods in the class.
- **Readability:** Reduces the number of variables to keep track of within a method.
- **Future Refactoring:** It often facilitates further refactorings like **Extract Method**.
