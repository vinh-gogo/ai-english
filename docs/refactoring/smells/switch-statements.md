# Switch Statements

**URL:** https://refactoring.guru/smells/switch-statements

## Signs and Symptoms
You have a complex `switch` operator or a sequence of `if` statements.

## Reasons for the Problem
- **Lack of Polymorphism:** Relatively rare use of `switch` and `case` operators is a hallmark of object-oriented code.
- **Scattered Logic:** Often, code for a single switch is scattered in different places in the program.
- **Maintenance Burden:** When a new condition is added, you have to find all the switch code and modify it.

## Treatment
- To isolate the switch and put it in the right class, use **Extract Method** and then **Move Method**.
- If the switch is based on type code, use **Replace Type Code with Subclasses** or **Replace Type Code with State/Strategy**.
- After specifying the inheritance structure, use **Replace Conditional with Polymorphism**.
- If there aren't too many conditions in the operator and they all call the same method with different parameters, use **Replace Parameter with Explicit Methods**.
- If one of the conditional options is `null`, use **Introduce Null Object**.

## Payoff
- Improved code organization: Logic is centralized and easier to follow.
- Extensibility: Adding new cases becomes easier (Open/Closed Principle).

## When to Ignore
- When a switch operator performs simple actions, there is no reason to make code changes.
- Switch operators are often used by factory design patterns (**Factory Method** or **Abstract Factory**) to select a class to create.
