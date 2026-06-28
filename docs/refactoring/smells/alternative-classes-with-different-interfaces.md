# Alternative Classes with Different Interfaces

**URL:** https://refactoring.guru/smells/alternative-classes-with-different-interfaces

## Signs and Symptoms
Two classes perform identical or very similar functions but have different method names, signatures, or interfaces.

## Reasons for the Problem
The programmer who created one of the classes likely didn't know that a functionally equivalent class already existed. This often happens in large projects or disconnected teams.

## Treatment
- **Rename Method:** Make method names identical across all alternative classes to align their interfaces.
- **Move Method, Add Parameter, and Parameterize Method:** Use these to make the signatures and implementations consistent.
- **Extract Superclass:** If the classes share only part of their functionality, move common parts into a superclass.
- **Delete Class:** Once duplication is resolved, you may be able to delete the redundant class.

## Payoff
- Reduced Duplication: Codebase becomes smaller and easier to maintain.
- Improved Readability: Developers don't have to guess why two classes do the same thing.
- Better Polymorphism: Unified interfaces allow polymorphic usage.
