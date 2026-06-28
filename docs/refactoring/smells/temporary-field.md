# Temporary Field

**URL:** https://refactoring.guru/smells/temporary-field

## Signs and Symptoms
- Temporary fields get their values (and thus are needed by objects) only under certain circumstances. Outside of these circumstances, they're empty.
- You often see `if (field != null)` checks before usage.

## Reasons for the Problem
- **Algorithm Parameters:** Often created for use in an algorithm that requires a large amount of inputs. Instead of passing many parameters, the programmer creates fields.
- **Single Use:** These fields only have meaning during the algorithm and are useless afterwards.
- **Confusion:** Readers expect fields to contain meaningful data throughout the object's life, but these are mostly empty.

## Treatment
- **Extract Class:** Move temporary fields and all code operating on them into a separate class (a **Method Object**).
- **Replace Method with Method Object:** Replace the old method with a new object dedicated to that algorithm.
- **Introduce Null Object:** Use a Null Object to replace conditional checks for the existence of values in the temporary field.

## Payoff
- Better code clarity and organization.
- Objects only contain fields they actually need.
- Complex algorithms are isolated, making testing easier.
