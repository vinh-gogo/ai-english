# Remove Setting Method

**URL:** https://refactoring.guru/remove-setting-method

## Problem
The value of a field should be set only when it's created and not change after that.

## Solution
Remove setter methods and make the field final or read-only.

## Why Refactor
- **Immutability:** Makes objects safer and more predictable.
- **Intent:** Explicitly signals that this data is not intended to be changed.
