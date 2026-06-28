# Split Temporary Variable

**URL:** https://refactoring.guru/split-temporary-variable

## Problem
You have a local variable that is used to store various intermediate values inside a method (except for cycle variables).

## Solution
Use different variables for different values. Each variable should be responsible for only one particular thing.

## Why Refactor
- **Clarity:** Using one variable for multiple purposes is confusing. Each variable should have a single, clear responsibility.
- **Maintainability:** It makes it easier to track the flow of values and prevents accidental usage of a value intended for a different purpose.
- **Naming:** You can give each variable a more specific and meaningful name.
