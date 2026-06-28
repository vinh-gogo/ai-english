# Replace Array with Object

**URL:** https://refactoring.guru/replace-array-with-object

## Problem
You have an array that contains various types of data.

## Solution
Replace the array with an object that has separate, named fields for each element.

## Why Refactor
- **Type Safety:** Prevents errors from accessing indices with the wrong type.
- **Readability:** Named fields are much more descriptive than numeric indices (e.g., `user.name` vs `row[0]`).
- **Maintainability:** Easier to add or remove fields without shifting all index positions.
