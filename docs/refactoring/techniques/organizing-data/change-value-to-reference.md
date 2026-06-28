# Change Value to Reference

**URL:** https://refactoring.guru/change-value-to-reference

## Problem
You have many identical instances of a single class that you need to replace with a single object.

## Solution
Convert the identical objects into a single reference object.

## Why Refactor
- **Consistency:** Ensures that changes to a conceptual object (like a specific Customer) are reflected everywhere it is used.
- **Memory Efficiency:** Avoids duplicating identical data across many objects.
