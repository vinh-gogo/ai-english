# Introduce Assertion

**URL:** https://refactoring.guru/introduce-assertion

## Problem
For a portion of code to work correctly, certain conditions or values must be true.

## Solution
Replace these implicit assumptions with explicit assertions that verify them.

## Why Refactor
- **Documentation:** Acts as live documentation of the method's prerequisites.
- **Early Detection:** Stops execution immediately when a bug occurs, preventing data corruption.
- **Debugging:** Pinpoints the exact location where an assumption fails.
