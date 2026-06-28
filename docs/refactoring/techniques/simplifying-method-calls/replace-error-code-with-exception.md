# Replace Error Code with Exception

**URL:** https://refactoring.guru/replace-error-code-with-exception

## Problem
A method returns a special value that indicates an error.

## Solution
Throw an exception instead.

## Why Refactor
- **Clean Logic:** Separates error handling from normal business flow.
- **Centralized Handling:** Exceptions can be caught at a higher level.
- **Safety:** Forces the caller to acknowledge the error.
