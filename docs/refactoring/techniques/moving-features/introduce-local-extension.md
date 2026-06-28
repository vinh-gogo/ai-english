# Introduce Local Extension

**URL:** https://refactoring.guru/introduce-local-extension

## Problem
A utility class lacks several methods you need, and you can't add them.

## Solution
Create a new class containing the methods and make it a child or wrapper of the utility class.

## Why Refactor
- **Organization:** Groups several "foreign methods" together in a logical home.
- **Reuse:** The extension class can be reused across the project.
- **Cleaner Client:** Clients use the extension class instead of having library-specific workarounds scattered everywhere.
