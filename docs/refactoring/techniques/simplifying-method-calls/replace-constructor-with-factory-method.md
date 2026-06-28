# Replace Constructor with Factory Method

**URL:** https://refactoring.guru/replace-constructor-with-factory-method

## Problem
You have a complex constructor that does more than just setting values.

## Solution
Create a factory method and use it to replace constructor calls.

## Why Refactor
- **Named Creation:** Factory methods have descriptive names (e.g., `createAdminUser()`).
- **Flexibility:** Can return subclasses or existing objects (like Singletons).
- **Overcoming Limitations:** Constructors are limited in their return types and naming.
