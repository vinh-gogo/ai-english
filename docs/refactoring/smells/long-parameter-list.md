# Long Parameter List

**URL:** https://refactoring.guru/smells/long-parameter-list

## Signs and Symptoms
More than three or four parameters for a method.

## Reasons for the Problem
- A long parameter list might happen after several types of algorithms are merged in a single method. A long list may have been created to control which algorithm is run and how.
- Long parameter lists may also be the byproduct of efforts to make classes more independent of each other. If several such objects are created and passed, the parameter list grows.

## Treatment
- Check what values are passed to parameters. If some of the arguments are just results of method calls of another object, use **Replace Parameter with Method Call**. This object can be placed in the field of its own class or passed as a method parameter.
- Instead of passing a group of data received from another object as parameters, pass the object itself to the method (**Preserve Whole Object**).
- If there are several unrelated data elements, sometimes you can merge them into a single parameter object (**Introduce Parameter Object**).

## Payoff
- Improved readability and shorter code.
- Refactoring may reveal previously unnoticed duplicate code.
- Reduced risk of parameter-swapping bugs and easier API evolution.
