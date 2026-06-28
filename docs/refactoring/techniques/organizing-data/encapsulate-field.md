# Encapsulate Field

**URL:** https://refactoring.guru/encapsulate-field

## Problem
You have a public field.

## Solution
Make the field private and create access methods (getters/setters) for it.

## Why Refactor
- **Encapsulation:** Protects internal state from direct external manipulation.
- **Control:** Allows the class to enforce validation or trigger side effects when the field is accessed or modified.
- **Evolution:** You can change the internal implementation without affecting clients.
