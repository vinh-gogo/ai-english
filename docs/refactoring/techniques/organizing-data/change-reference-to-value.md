# Change Reference to Value

**URL:** https://refactoring.guru/change-reference-to-value

## Problem
You have a reference object that is too small and infrequently changed to justify managing its life cycle.

## Solution
Turn it into a value object.

## Why Refactor
- **Simplicity:** Value objects are immutable and don't require tracking of references.
- **Safety:** Avoids unintended side effects when multiple objects share the same reference.
- **Concurrency:** Immutable objects are inherently thread-safe.
