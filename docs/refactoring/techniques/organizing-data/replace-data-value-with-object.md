# Replace Data Value with Object

**URL:** https://refactoring.guru/replace-data-value-with-object

## Problem
A class contains a data field (like a string or number) that has its own associated data and behavior.

## Solution
Create a new class, move the old field and its behavior into it, and store an instance of this new class in the original class.

## Why Refactor
- **Cohesion:** Groups related data and logic together.
- **Scalability:** Makes it easy to add more fields or behaviors related to that concept later.
- **Clarity:** Replaces raw primitives with meaningful domain objects.
