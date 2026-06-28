# Replace Delegation with Inheritance

**URL:** https://refactoring.guru/replace-delegation-with-inheritance

## Problem
A class contains many simple methods that do nothing but delegate to all the methods of another class.

## Solution
Make the class inherit from the delegate class, which makes the delegating methods unnecessary.

## Why Refactor
- **Boilerplate Reduction:** Eliminates dozens of "pass-through" methods.
- **Natural Expressiveness:** Sometimes the relationship really is "is-a" and inheritance is simpler.
