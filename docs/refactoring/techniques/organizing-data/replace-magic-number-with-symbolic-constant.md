# Replace Magic Number with Symbolic Constant

**URL:** https://refactoring.guru/replace-magic-number-with-symbolic-constant

## Problem
Your code uses a literal number that has a specific meaning but no explanation.

## Solution
Replace the number with a constant that has a human-readable name.

## Why Refactor
- **Documentation:** The constant name explains the intent (e.g., `MAX_LOGIN_ATTEMPTS`).
- **Maintainability:** Easier to update the value in one place rather than searching for all occurrences of the number.
- **Reliability:** Reduces the risk of errors when the same number is used for different purposes.
