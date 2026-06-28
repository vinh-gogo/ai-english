# Divergent Change

**URL:** https://refactoring.guru/smells/divergent-change

## Signs and Symptoms
You find yourself having to change many unrelated methods within a single class when making a change (e.g., changing the database logic requires changing product display logic).

## Reasons for the Problem
- **Poor Structure:** The class has too many responsibilities (violates Single Responsibility Principle).
- **Evolutionary Bloat:** Features were added over time without creating new abstractions.

## Treatment
- **Extract Class:** Split the class into several classes, each with its own single responsibility.
- **Move Method / Move Field:** Relocate behavior to more appropriate classes.
- **Extract Superclass / Extract Subclass:** Combine common behavior via inheritance if applicable.

## Payoff
- Improves code organization: Each class has one job.
- Reduces risk: Changes to one feature don't impact unrelated parts of the same class.
- Compliance with SOLID (SRP).
