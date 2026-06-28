# Replace Subclass with Fields

**URL:** https://refactoring.guru/replace-subclass-with-fields

## Problem
You have subclasses that differ only in the constant values returned by their methods.

## Solution
Move these values into fields in the parent class and delete the subclasses.

## Why Refactor
- **Simplicity:** Eliminates unnecessary class hierarchies that don't add behavioral value.
- **Slimming:** Reduces the number of classes to manage.
