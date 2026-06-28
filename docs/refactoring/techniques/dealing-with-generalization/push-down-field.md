# Push Down Field

**URL:** https://refactoring.guru/push-down-field

## Problem
A field is used only in a few subclasses.

## Solution
Move the field to those specific subclasses.

## Why Refactor
- **Clean Hierarchy:** Superclasses should only contain data applicable to all children.
- **Isolation:** Keeps specialized data within specialized classes.
