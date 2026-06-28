# Replace Type Code with State/Strategy

**URL:** https://refactoring.guru/replace-type-code-with-state-strategy

## Problem
You have a type code that affects behavior, but you cannot use subclasses.

## Solution
Replace the type code with a State or Strategy object.

## Why Refactor
- **Dynamic Behavior:** Allows the object's "type" (state) to change at runtime.
- **Polymorphism Benefits:** Still allows you to remove complex conditionals.
- **Decoupling:** Separates the type-specific behavior into independent strategy classes.
