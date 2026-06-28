# Shotgun Surgery

**URL:** https://refactoring.guru/smells/shotgun-surgery

## Signs and Symptoms
Making a single modification requires making many small changes to many different classes. It is the opposite of **Divergent Change**.

## Reasons for the Problem
A single responsibility has been split up and spread across too many classes. This often happens after over-zealous attempts to solve Divergent Change by splitting code too thin without maintaining cohesion.

## Treatment
- **Move Method & Move Field:** Consolidate behaviors and data into a single class.
- **Create New Class:** If no existing class is suitable, create a new container for the logic.
- **Inline Class:** Eliminate redundant classes by merging them into their destination.

## Payoff
- Better organized code: Related logic is kept together.
- Fewer places to track: Changes are localized.
- Easier maintenance and tracing.
