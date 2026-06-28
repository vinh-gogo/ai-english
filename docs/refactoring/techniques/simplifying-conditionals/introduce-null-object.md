# Introduce Null Object

**URL:** https://refactoring.guru/introduce-null-object

## Problem
Because methods return `null` instead of real objects, you have many checks for `null` in your code.

## Solution
Instead of returning `null`, return a null object that exhibits default behavior (often doing nothing).

## Why Refactor
- **Clean Code:** Eliminates repetitive `if (obj != null)` checks.
- **Safety:** Prevents `NullPointerException`.
- **Simplification:** Allows you to treat "no data" the same as "actual data" in polymorphic calls.
