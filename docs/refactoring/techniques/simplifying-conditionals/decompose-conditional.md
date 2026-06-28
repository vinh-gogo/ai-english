# Decompose Conditional

**URL:** https://refactoring.guru/decompose-conditional

## Problem
You have a complex conditional (`if-then/else` or `switch`).

## Solution
Decompose the complicated parts of the conditional into separate methods: the condition, the `then` part, and the `else` part.

## Why Refactor
- **Readability:** Clearly named methods explain the logic (e.g., `isSummerRate()`).
- **Clarity:** Prevents the reader from getting lost in nested logic and forgetting the original condition.
- **Self-Documentation:** The code explains *why* a certain branch is taken.
