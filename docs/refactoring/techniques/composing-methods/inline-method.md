# Inline Method

**URL:** https://refactoring.guru/inline-method

## Problem
When a method body is more obvious than the method itself.

## Solution
Replace calls to the method with the method's content and delete the method itself.

## Why Refactor
- **Needless Indirection:** Sometimes a method is not doing enough to justify its existence, or it was poorly extracted. Inlining it simplifies the code by removing unnecessary layers.
- **Bad Factoring:** If a group of methods is badly factored, inlining them allows you to see the whole logic and then re-extract them more logically.
- **Clarity:** If the method name doesn't add value beyond what the code itself says, inlining simplifies the flow.
