# Incomplete Library Class

**URL:** https://refactoring.guru/smells/incomplete-library-class

## Signs and Symptoms
A library no longer meets needs, but you can't change it because it's read-only or third-party.

## Reasons for the Problem
Author missed a feature or refused to implement it for being too specific.

## Treatment
- **Introduce Foreign Method:** For adding just one or two simple methods.
- **Introduce Local Extension:** For significant changes (subclassing or wrapper).

## Payoff
- Reduced duplication: "Piggy-back" on existing library instead of rewriting.
- Cleaner code: Separation of extension logic.
