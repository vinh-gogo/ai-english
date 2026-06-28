# Move Field

**URL:** https://refactoring.guru/move-field

## Problem
A field is used more in another class than in its own class.

## Solution
Create the field in the new class and redirect all users of the old field to it.

## Why Refactor
- **Cohesion:** Data should live where it is most used.
- **Prerequisite:** Often performed as part of **Extract Class**.
- **Maintainability:** Easier to manage state when it is located in the most relevant context.
