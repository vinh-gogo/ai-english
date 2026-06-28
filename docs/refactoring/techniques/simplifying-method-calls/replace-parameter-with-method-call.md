# Replace Parameter with Method Call

**URL:** https://refactoring.guru/replace-parameter-with-method-call

## Problem
Calling a query method and passing its results as parameters, while the receiver could call the query directly.

## Solution
Remove the parameter and put the method call inside the method body.

## Why Refactor
- **Simpler Calls:** The caller doesn't have to prepare the data.
- **Consistency:** Ensures the method always uses the most up-to-date data.
