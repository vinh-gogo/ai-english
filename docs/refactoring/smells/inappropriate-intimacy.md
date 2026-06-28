# Inappropriate Intimacy

**URL:** https://refactoring.guru/smells/inappropriate-intimacy

## Signs and Symptoms
One class uses the internal fields and methods of another class.

## Reasons for the Problem
Classes are too tightly coupled ("spending too much time together"). Good classes should know as little as possible about each other.

## Treatment
- **Move Method & Move Field:** Relocate logic/data to where they are actually used.
- **Extract Class & Hide Delegate:** Formally organize the relationship and hide implementation details.
- **Change Bidirectional to Unidirectional:** If mutually interdependent.
- **Replace Inheritance with Delegation:** If the intimacy is between subclass and superclass.

## Payoff
- Improves organization.
- Simpler support and better code reuse.
