# Inline Class

**URL:** https://refactoring.guru/inline-class

## Problem
A class does almost nothing and isn't responsible for anything.

## Solution
Move all features from the class to another one and delete the empty class.

## Why Refactor
- **Reduce Complexity:** Eliminates unnecessary layers and empty classes that add "noise" to the project.
- **Consolidation:** Useful when a class has shrunk after its responsibilities were moved elsewhere.
