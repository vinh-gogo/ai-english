# Primitive Obsession

**URL:** https://refactoring.guru/smells/primitive-obsession

## Signs and Symptoms
- Use of primitives instead of small objects for simple tasks (such as currency, ranges, special strings for phone numbers, etc.).
- Use of constants for coding information (such as a constant `USER_ADMIN_ROLE = 1` for referring to users with admin rights).
- Use of string constants as field names for use in dynamic arrays.

## Reasons for the Problem
- Like most other smells, primitive obsession is born in moments of weakness. "Just a field for storing a phone number," the programmer thinks. Then the same thing happens for the address, then for the zip code.
- Primitives are used because they are easier to implement than a whole new class.
- Sometimes the programmer is just too lazy to create a new class for a small amount of data.

## Treatment
- If you have a large variety of primitive fields, it may be possible to logically group some of them into their own class. Even better, move the behavior associated with this data into the class too. Try **Replace Data Value with Object**.
- If the value of a primitive field is used in method parameters, use **Introduce Parameter Object** or **Preserve Whole Object**.
- When complicated data is coded in variables, use **Replace Type Code with Class**, **Replace Type Code with Subclasses** or **Replace Type Code with State/Strategy**.
- If there are arrays among the variables, use **Replace Array with Object**.

## Payoff
- Code becomes more flexible due to the use of objects instead of primitives.
- Better understanding and organization of code. Operations on particular data are in the same place, instead of being scattered.
- No more need to guess the meaning of a bunch of primitive fields.
- Type safety and encapsulation of validation/formatting logic.
