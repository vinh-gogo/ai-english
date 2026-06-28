# Inline Temp

**URL:** https://refactoring.guru/inline-temp

## Problem
You have a temporary variable that is assigned the result of a simple expression and nothing more.

## Solution
Replace the references to the variable with the expression itself.

## Why Refactor
- **Prerequisite:** This technique is often used as a step toward **Replace Temp with Query** or to clean up code before **Extract Method**.
- **Simplicity:** If a temporary variable doesn't add clarity and is only used once, it can get in the way.
