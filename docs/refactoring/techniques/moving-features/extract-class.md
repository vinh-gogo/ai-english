# Extract Class

**URL:** https://refactoring.guru/extract-class

## Problem
One class does the work of two (violates Single Responsibility Principle).

## Solution
Create a new class and move the relevant fields and methods from the old class into the new one.

## Why Refactor
- **Single Responsibility:** Ensures each class has one clear job.
- **Maintainability:** Smaller, focused classes are easier to understand, test, and modify.
- **Reuse:** The extracted logic can now be easily reused by other parts of the system.
