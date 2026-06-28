# Replace Conditional with Polymorphism

**URL:** https://refactoring.guru/replace-conditional-with-polymorphism

## Problem
You have a conditional that performs various actions depending on object type or properties.

## Solution
Create subclasses matching the branches of the conditional. Implement a shared method in each, and move the code from the corresponding branch into it. Replace the conditional with a call to this method.

## Why Refactor
- **Open/Closed Principle:** Allows adding new types without modifying existing code.
- **Cleanliness:** Eliminates bulky `switch` or `if-else` blocks.
- **"Tell, Don't Ask":** Shifts responsibility to the objects themselves.
