# Extract Interface

**URL:** https://refactoring.guru/extract-interface

## Problem
Multiple clients use the same subset of a class's features, or two classes share part of their interfaces.

## Solution
Move that subset of features into its own interface.

## Why Refactor
- **Loose Coupling:** Clients depend on the interface, not the concrete implementation.
- **Testability:** Facilitates mocking and stubbing.
- **Flexibility:** Allows different implementations to be used interchangeably.
