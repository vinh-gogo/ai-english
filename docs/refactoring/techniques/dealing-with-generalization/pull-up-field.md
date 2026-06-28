# Pull Up Field

**URL:** https://refactoring.guru/pull-up-field

## Problem
Two subclasses have the same field.

## Solution
Remove the field from subclasses and move it to the superclass.

## Why Refactor
- **Eliminate Duplication:** Removes redundant data definitions.
- **Maintenance:** Prevents fields from diverging in their purpose or logic.
