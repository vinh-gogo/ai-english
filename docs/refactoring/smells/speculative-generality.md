# Speculative Generality

**URL:** https://refactoring.guru/smells/speculative-generality

## Signs and Symptoms
Unused abstract classes, methods, parameters, or fields created "just in case."

## Reasons for the Problem
Over-engineering: Trying to predict future needs and violating YAGNI (*You Ain't Gonna Need It*).

## Treatment
- **Collapse Hierarchy:** Delete unnecessary abstract classes.
- **Inline Class:** Remove pointless delegation.
- **Inline Method / Remove Parameter:** Delete unused methods or arguments.

## Payoff
- Focused, leaner code.
- Easier to understand actual requirements.

## When to Ignore
- Building a Framework (where users will need features you don't use).
- Code required for Unit Testing.
