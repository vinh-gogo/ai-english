# Introduce Foreign Method

**URL:** https://refactoring.guru/introduce-foreign-method

## Problem
A utility class doesn't contain the method that you need and you can't add the method to the class (e.g., third-party library).

## Solution
Add the method to a client class and pass an instance of the utility class to it as an argument.

## Why Refactor
- **Reduce Duplication:** Avoids re-implementing the same workaround in multiple places.
- **Intent:** Clearly documents that this method is an extension of the library's functionality.
