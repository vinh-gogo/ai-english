# Extract Subclass

**URL:** https://refactoring.guru/extract-subclass

## Problem
A class has features that are used only in certain cases.

## Solution
Create a subclass and move those specific features into it.

## Why Refactor
- **SRP:** Moves specialized logic out of a generic class.
- **Extensibility:** Makes it easier to add new specialized behaviors later.
