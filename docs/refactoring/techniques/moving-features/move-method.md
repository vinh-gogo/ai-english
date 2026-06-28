# Move Method

**URL:** https://refactoring.guru/move-method

## Problem
A method is used more in another class than in its own class.

## Solution
Create a new method in the class that uses the method the most, then move the code from the old method to there. Turn the code of the original method into a reference to the new method or remove it entirely.

## Why Refactor
- **Cohesion:** Groups related data and behavior together.
- **Decoupling:** Reduces the dependency of other classes on the current class.
- **Clarity:** Makes the classes' responsibilities more logical.
