# Remove Middle Man

**URL:** https://refactoring.guru/remove-middle-man

## Problem
A class has too many methods that do nothing but delegate to other objects.

## Solution
Delete these methods and force the client to call the end methods directly.

## Why Refactor
- **Needless Indirection:** If a class becomes just a "pass-through," it adds unnecessary complexity.
- **Simplification:** Reduces the number of boilerplate methods.
- **Directness:** Makes the actual logic more visible.
