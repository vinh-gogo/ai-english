# Replace Exception with Test

**URL:** https://refactoring.guru/replace-exception-with-test

## Problem
You throw an exception in a place where a simple test would do the job.

## Solution
Replace the catch block with an `if` condition test.

## Why Refactor
- **Performance:** Exceptions are more expensive than conditional checks.
- **Readability:** Conditional checks are more explicit for expected situations.
- **Appropriate Use:** Exceptions should be for "exceptional" circumstances, not regular flow.
