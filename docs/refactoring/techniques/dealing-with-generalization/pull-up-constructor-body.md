# Pull Up Constructor Body

**URL:** https://refactoring.guru/pull-up-constructor-body

## Problem
Your subclasses have constructors with code that is mostly identical.

## Solution
Create a superclass constructor and move the combined code there. Call it from the subclass constructors.

## Why Refactor
- **Reduce Duplication:** Consolidates common initialization logic.
- **Reliability:** Ensures important setup happens consistently for all subclasses.
