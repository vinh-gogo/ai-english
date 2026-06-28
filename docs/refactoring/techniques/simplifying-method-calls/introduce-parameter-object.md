# Introduce Parameter Object

**URL:** https://refactoring.guru/introduce-parameter-object

## Problem
Your methods contain a repeating group of parameters.

## Solution
Replace these parameters with a single object (DTO).

## Why Refactor
- **Organization:** Groups related data into a logical container (e.g., `DateRange`).
- **Shorten Signatures:** Makes method calls much cleaner.
- **Grouping Logic:** Often reveals behavior that should be moved into the new object.
