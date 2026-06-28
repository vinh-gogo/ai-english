# Data Clumps

**URL:** https://refactoring.guru/smells/data-clumps

## Signs and Symptoms
Sometimes different parts of the code contain identical groups of variables (such as parameters for connecting to a database). These clumps should be turned into their own classes.

## Reasons for the Problem
Often these data groups are due to poor program structure or "copypasta" programming. Data that belongs together is not formally organized into an object.

## Treatment
- If repeating data comprises the fields of a class, use **Extract Class** to move the fields to their own class.
- If the same data clumps are passed in the parameters of methods, use **Introduce Parameter Object** to set them off as a class.
- If some of the data is passed to other methods, think about passing the entire data object instead of individual fields (**Preserve Whole Object**).
- Look at the code used by these fields. It might be a good idea to move this code to a data class.

## Payoff
- Improves code organization. Operations on particular data are now gathered in a single place.
- Reduces code size.
- Natural home for logic that operates on that data.

## How to Spot
A good test is to delete one of the data values and see whether the others still make sense. If this isn't the case, this is a good sign that this group of variables should be combined into an object.
