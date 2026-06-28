# Hide Method

**URL:** https://refactoring.guru/hide-method

## Problem
A method isn't used by other classes or is used only inside its own class hierarchy.

## Solution
Make the method private or protected.

## Why Refactor
- **API Surface Area:** Smaller public APIs are easier to maintain and understand.
- **Encapsulation:** Keeps internal implementation details hidden.
