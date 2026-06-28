# Replace Inheritance with Delegation

**URL:** https://refactoring.guru/replace-inheritance-with-delegation

## Problem
A subclass uses only a portion of the methods of its superclass, or it's no longer logically an "is-a" relationship.

## Solution
Create a field in the subclass to hold the superclass object, delegate the necessary methods to it, and remove the inheritance.

## Why Refactor
- **Composition over Inheritance:** Provides a more flexible relationship.
- **Cleanliness:** Avoids inheriting dozens of methods that aren't needed.
- **Encapsulation:** Keeps the "delegate" object internal.
