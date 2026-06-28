# Parallel Inheritance Hierarchies

**URL:** https://refactoring.guru/smells/parallel-inheritance-hierarchies

## Signs and Symptoms
Whenever you create a subclass for one class, you find yourself needing to create a subclass for another class. They often share similar name prefixes or suffixes.

## Reasons for the Problem
- **Natural Growth:** Initially, two hierarchies might seem logical.
- **Lack of Abstraction:** As the system grows, the maintenance of synchronization between hierarchies becomes difficult.
- **Over-dependency:** One hierarchy depends on another via composition in a way that forces mirroring.

## Treatment
- **Referencing:** Make instances of one hierarchy refer to instances of the other.
- **Merging:** Use **Move Method** and **Move Field** to move logic from the "referenced" hierarchy to the "referencing" one, then eliminate the empty hierarchy.

## Payoff
- Reduces code duplication.
- Simplifies organization.
- Easier maintenance: Add features in one place instead of two.
