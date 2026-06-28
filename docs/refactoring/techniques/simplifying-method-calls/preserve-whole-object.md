# Preserve Whole Object

**URL:** https://refactoring.guru/preserve-whole-object

## Problem
You get several values from an object and then pass them as parameters to a method.

## Solution
Pass the whole object instead.

## Why Refactor
- **Shorten Parameter Lists:** Reduces the number of arguments.
- **Future Proofing:** If the method needs more data from that object later, you don't need to change the signature.
- **Readability:** The method call is cleaner.
