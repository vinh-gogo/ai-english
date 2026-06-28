# Self Encapsulate Field

**URL:** https://refactoring.guru/self-encapsulate-field

## Problem
You use direct access to private fields inside a class.

## Solution
Create a getter and setter for the field and use only these methods to access the field.

## Why Refactor
- **Flexibility:** Allows you to add logic (like validation or lazy loading) inside the accessors later without changing all usages.
- **Inheritance:** Subclasses can override the accessors to change the field's behavior.
