# Replace Parameter with Explicit Methods

**URL:** https://refactoring.guru/replace-parameter-with-explicit-methods

## Problem
A method is split into parts, each of which is run depending on the value of a parameter.

## Solution
Extract the individual parts of the method into their own methods and call them directly.

## Why Refactor
- **Clarity:** Replaces complex branching logic with specific, clearly named methods (e.g., `setHeight(v)` instead of `setValue("height", v)`).
- **Compile-time Safety:** Allows for better type checking.
