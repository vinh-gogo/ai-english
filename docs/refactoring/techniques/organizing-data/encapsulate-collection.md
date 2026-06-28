# Encapsulate Collection

**URL:** https://refactoring.guru/encapsulate-collection

## Problem
A class contains a collection field and a simple getter/setter for it.

## Solution
Make the getter return a read-only view or a copy, and provide specific methods for adding/removing elements.

## Why Refactor
- **Integrity:** Prevents clients from bypassing the class's logic when modifying the collection.
- **Abstraction:** The class maintains control over its internal data structures.
