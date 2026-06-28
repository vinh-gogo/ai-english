# Change Unidirectional Association to Bidirectional

**URL:** https://refactoring.guru/change-unidirectional-association-to-bidirectional

## Problem
Two classes need to use each other's features, but the association between them is only one-way.

## Solution
Add the missing association to the class that needs it.

## Why Refactor
- **Navigability:** Allows you to find associated objects from both sides.
- **Flexibility:** Necessary when requirements evolve to require reverse lookups.
