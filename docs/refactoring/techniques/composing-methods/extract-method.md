# Extract Method

**URL:** https://refactoring.guru/extract-method

## Problem
You have a code fragment that can be grouped together.

## Solution
Move this code to a separate new method (or function) and replace the old code with a call to the method.

## Why Refactor
- **Readability:** The more lines in a method, the harder it is to figure out what the method does. Extracting code into a method with a descriptive name makes the intent clear.
- **Code Duplication:** Often, the code found in a method can be reused in other places. Extracting it allows you to replace duplicates with a single method call.
- **Isolation:** It isolates independent parts of code, making errors less likely (such as accidentally modifying the wrong variable).
- **Foundation:** Extracting methods is a prerequisite step for many other refactoring patterns.
