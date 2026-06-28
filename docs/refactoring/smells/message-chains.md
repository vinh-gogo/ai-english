# Message Chains

**URL:** https://refactoring.guru/smells/message-chains

## Signs and Symptoms
A series of consecutive method calls like `a.getB().getC().doSomething()`.

## Reasons for the Problem
- **Violicated Encapsulation:** Client is forced to navigate the class structure.
- **Dependency:** Any change in intermediate classes breaks the client.

## Treatment
- **Hide Delegate:** Create a method in the first class to perform the action for the client.
- **Extract & Move Method:** Ask *why* the end object is used, extract that logic, and move it up the chain.

## Payoff
- Reduced dependencies.
- Reduced boilerplate in the client.
- Easier maintenance: Fix intermediate structure in one place.
