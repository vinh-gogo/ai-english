# Replace Type Code with Class

**URL:** https://refactoring.guru/replace-type-code-with-class

## Problem
A class has a field containing type code (like an integer or string) that doesn't affect program behavior.

## Solution
Create a new class and use its objects instead of the raw type code values.

## Why Refactor
- **Type Safety:** The compiler can verify that only valid type objects are used.
- **Readability:** Replaces obscure numbers or strings with descriptive class names.
