# Form Template Method

**URL:** https://refactoring.guru/form-template-method

## Problem
Subclasses implement algorithms that contain similar steps, but the steps themselves differ.

## Solution
Move the algorithm structure to a superclass method, and leave the implementation of the varying steps to the subclasses.

## Why Refactor
- **Capture Skeleton:** Standardizes the algorithm's process flow.
- **Code Reuse:** Prevents duplication of the structural parts of the algorithm.
