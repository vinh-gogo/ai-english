# Remove Assignments to Parameters

**URL:** https://refactoring.guru/remove-assignments-to-parameters

## Problem
Some value is assigned to a parameter inside a method's body.

## Solution
Use a local variable instead of assigning directly to a parameter.

## Why Refactor
- **Avoid Confusion:** Prevents confusion between pass-by-value and pass-by-reference behaviors in different languages.
- **Intent Clarity:** Parameters should be treated as input data ("read-only"). Using local variables for changes makes the intent explicit.
- **Trackability:** Preserves the original value passed into the method for comparison or auditing.
