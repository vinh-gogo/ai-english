# Consolidate Conditional Expression

**URL:** https://refactoring.guru/consolidate-conditional-expression

## Problem
You have multiple conditionals that lead to the same result or action.

## Solution
Consolidate all these conditionals into a single expression using logical operators.

## Why Refactor
- **Remove Duplication:** Eliminates repeated actions in multiple blocks.
- **Clarity:** Clarifies that several checks are part of one complex condition leading to a single outcome.
- **Foundation:** Makes it easier to perform **Extract Method** on the resulting condition.
