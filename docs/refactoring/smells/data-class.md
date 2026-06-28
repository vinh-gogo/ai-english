# Data Class

**URL:** https://refactoring.guru/smells/data-class

## Signs and Symptoms
A class that contains only fields and crude methods for accessing them (getters/setters). They are merely containers for data used by other classes.

## Reasons for the Problem
- Simple modeling without adding behavior.
- Logic that *should* belong to this data is scattered across client classes.

## Treatment
- **Encapsulate Field / Collection:** To hide internal data.
- **Move Method / Extract Method:** Migrate behavior that operates on the data from client classes to the data class itself.
- **Remove Setting Method / Hide Method:** Remove unnecessary accessors.

## Payoff
- Improved encapsulation and organization.
- Centralized data operations, making it easier to find duplication in client code.
