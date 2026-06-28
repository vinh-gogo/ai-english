# Extract Variable

**URL:** https://refactoring.guru/extract-variable

## Problem
You have an expression that is hard to understand.

## Solution
Place the result of the expression or its parts in separate variables that are self-explanatory.

## Why Refactor
- **Complex Expressions:** The main goal is to break down complex logic (like long `if` conditions) into intermediate parts.
- **Self-Documentation:** By giving these parts descriptive names, you document the intent of the code without needing comments.
- **Debugging:** It makes it easier to inspect intermediate values during debugging.
