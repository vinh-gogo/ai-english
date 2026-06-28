# Dead Code

**URL:** https://refactoring.guru/smells/dead-code

## Signs and Symptoms
A variable, parameter, field, method, or class is no longer used.

## Reasons for the Problem
- Requirements changed but old code wasn't cleaned up.
- Conditionals that can never be reached.
- Fear of breaking things by deleting code.

## Treatment
- **Delete directly:** Use IDE tools to find and safely remove unused code.
- **Inline Class / Collapse Hierarchy:** For redundant classes.
- **Remove Parameter:** For unused parameters.

## Payoff
- Reduced codebase size.
- Less confusion for developers.
- Easier onboarding and maintenance.
