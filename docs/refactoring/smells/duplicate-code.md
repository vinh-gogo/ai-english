# Duplicate Code

**URL:** https://refactoring.guru/smells/duplicate-code

## Signs and Symptoms
Two or more code fragments look almost identical.

## Reasons for the Problem
- Multiple programmers working on different parts of the project without knowing about similar code.
- Pressure to meet deadlines, leading to "copy-paste" programming.

## Treatment
- **Extract Method:** For duplication in the same class.
- **Pull Up Field/Method:** For duplication in subclasses.
- **Form Template Method:** For similar but not identical algorithms.
- **Substitute Algorithm:** Replace complex duplicated logic with a simpler one.
- **Extract Superclass / Class:** For duplication across different classes.

## Payoff
- Shorter, cleaner code.
- Centralized maintenance: Fix a bug once instead of in every copy.
