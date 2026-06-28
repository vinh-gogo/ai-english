# Pull Up Method

**URL:** https://refactoring.guru/pull-up-method

## Problem
Subclasses have methods that perform similar work.

## Solution
Make the methods identical and then move them to the superclass.

## Why Refactor
- **Centralized Logic:** Fix bugs or add features in one place instead of many.
- **Consistency:** Ensures all subclasses share the same behavior.
