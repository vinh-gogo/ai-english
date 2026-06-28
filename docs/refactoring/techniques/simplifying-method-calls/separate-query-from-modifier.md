# Separate Query from Modifier

**URL:** https://refactoring.guru/separate-query-from-modifier

## Problem
A method returns a value but also changes something inside an object.

## Solution
Split the method into two separate methods: one for the query (returns value) and one for the modifier (changes state).

## Why Refactor
- **CQS Principle:** A "question" shouldn't change the "answer."
- **Idempotency:** Queries become safe to call multiple times without side effects.
- **Clarity:** Clearly distinguishes between read and write operations.
