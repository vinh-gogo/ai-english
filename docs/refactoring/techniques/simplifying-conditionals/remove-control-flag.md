# Remove Control Flag

**URL:** https://refactoring.guru/remove-control-flag

## Problem
You have a boolean variable that acts as a control flag for multiple boolean expressions.

## Solution
Instead of using a variable, use `break`, `continue`, or `return` to control the flow directly.

## Why Refactor
- **Simplicity:** Eliminates the need to track the state of a flag variable.
- **Explicitness:** Built-in keywords make the control flow intention immediate and clear.
- **Readability:** Reduces nesting and verbosity.
