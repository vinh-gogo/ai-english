# Feature Envy

**URL:** https://refactoring.guru/smells/feature-envy

## Signs and Symptoms
A method accesses the data of another object more than its own data. It seems more interested in the features of a different class.

## Reasons for the Problem
- **Misplaced Logic:** Often occurs after fields moved to a Data Class, but the operations stayed behind.
- **Evolutionary Drift:** A method grew to rely more on external data over time.

## Treatment
Rule: **If things change at the same time, they should be in the same place.**
- **Move Method:** If a method belongs in another class.
- **Extract Method:** If only part of a method is envious.
- **Identify Home:** Move the method to the class containing the *most* data it uses.

## Payoff
- Reduced duplication.
- Cohesion: Logic is next to the data it operates on.
- Lower coupling.

## When to Ignore
- Patterns like Strategy, Visitor, or State where behavior is intentionally separate.
- Service Layers designed for coordination.
