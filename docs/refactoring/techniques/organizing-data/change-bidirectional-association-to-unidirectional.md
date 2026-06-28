# Change Bidirectional Association to Unidirectional

**URL:** https://refactoring.guru/change-bidirectional-association-to-unidirectional

## Problem
You have a two-way association between classes, but one of the classes no longer uses the other's features.

## Solution
Remove the unused association.

## Why Refactor
- **Reduced Coupling:** Minimizes dependencies between classes.
- **Simplicity:** Bidirectional links are harder to maintain and synchronize.
- **Memory Safety:** Helps prevent circular references and memory leaks in some environments.
