# Push Down Method

**URL:** https://refactoring.guru/push-down-method

## Problem
Behavior implemented in a superclass is used by only one (or a few) subclasses.

## Solution
Move the behavior to the specific subclasses that actually use it.

## Why Refactor
- **Superclass Clarity:** Keeps the superclass focused on truly general behavior.
- **Reduced Bloat:** Subclasses that don't need the behavior aren't burdened with it.
