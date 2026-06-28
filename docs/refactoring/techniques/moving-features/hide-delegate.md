# Hide Delegate

**URL:** https://refactoring.guru/hide-delegate

## Problem
The client gets object B from a field or method of object A, then calls a method of object B.

## Solution
Create a new method in class A that delegates the call to object B. Now the client doesn't know about or depend on class B.

## Why Refactor
- **Encapsulation:** Protects the client from knowledge of the internal relationships between objects.
- **Decoupling:** Makes it easier to change the relationship between A and B without breaking the client.
- **Law of Demeter:** Follows the principle of "don't talk to strangers."
