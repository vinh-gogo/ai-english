# Comments

**URL:** https://refactoring.guru/smells/comments

## Signs and Symptoms
A method is filled with explanatory comments because the code itself isn't intuitive.

## Reasons for the Problem
Comments are often used as "deodorant" for poorly written code. The author realizes the logic is complex but chooses to explain it with text rather than simplifying the code structure.

## Treatment
- **Extract Variable:** If the comment explains a complex expression.
- **Extract Method:** If the comment explains a block of code (use the comment content for the method name).
- **Rename Method:** If the method intent is unclear.
- **Introduce Assertion:** To explain system state rules.

## Payoff
- Self-documenting code: The intent is clear from the code itself.
- Improved readability and maintenance.

## When to Ignore
- Explaining *why* (design decisions) rather than *what*.
- Extremely complex algorithms that cannot be further simplified.
