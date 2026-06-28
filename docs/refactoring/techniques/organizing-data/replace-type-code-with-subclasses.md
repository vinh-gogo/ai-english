# Replace Type Code with Subclasses

**URL:** https://refactoring.guru/replace-type-code-with-subclasses

## Problem
You have a type code that directly affects program behavior (used in `if` or `switch` statements).

## Solution
Create subclasses for each value of the type code and move the relevant behaviors into these subclasses.

## Why Refactor
- **Polymorphism:** Replaces complex conditionals with polymorphic method calls.
- **Extensibility:** Adding a new type becomes a matter of adding a new subclass.
- **SRP:** Moves type-specific logic out of the main class.
