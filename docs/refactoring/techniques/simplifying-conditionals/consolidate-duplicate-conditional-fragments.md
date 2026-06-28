# Consolidate Duplicate Conditional Fragments

**URL:** https://refactoring.guru/consolidate-duplicate-conditional-fragments

## Problem
Identical code can be found in all branches of a conditional.

## Solution
Move the common code outside of the conditional.

## Why Refactor
- **Reduce Duplication:** Shorter code and easier maintenance.
- **Reliability:** Ensures that changes to common logic only need to be made in one place.
- **Clarity:** Highlights the actual differences between branches.
