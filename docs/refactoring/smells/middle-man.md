# Middle Man

**URL:** https://refactoring.guru/smells/middle-man

## Signs and Symptoms
A class performs only one action: delegating work to another class. "Why does this class even exist?"

## Reasons for the Problem
- **Over-zealous Hiding:** Result of over-eliminating Message Chains.
- **Gradual Logic Migration:** Useful work was moved elsewhere, leaving a hollow shell.

## Treatment
- **Remove Middle Man:** Let the client call the end object directly.
- **Inline Method:** If only a few methods are delegating.

## Payoff
- Less bulky code (fewer empty classes).
- Improved architectural clarity.

## When to Ignore
- Architectures like Proxy, Decorator, or Facade where delegation adds value or control.
