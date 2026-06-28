# 05-criticism

Source: https://refactoring.guru/design-patterns/criticism

/ [Design Patterns](/design-patterns)

# Criticism of patterns

It seems like only lazy people haven’t criticized design patterns yet. Let’s take a look at the most typical arguments against using patterns.

#### Kludges for a weak programming language This point of view was first expressed by Paul Graham in the essay [Revenge of the Nerds](http://www.paulgraham.com/icad.html). Read more about this on this [Wiki page](http://wiki.c2.com/?AreDesignPatternsMissingLanguageFeatures)

Usually the need for patterns arises when people choose a programming language or a technology that lacks the necessary level of abstraction. In this case, patterns become a kludge that gives the language much-needed super-abilities.

For example, the [Strategy](/design-patterns/strategy) pattern can be implemented with a simple anonymous (lambda) function in most modern programming languages.

#### Inefficient solutions

Patterns try to systematize approaches that are already widely used. This unification is viewed by many as a dogma, and they implement patterns “to the letter”, without adapting them to the context of their project.

#### Unjustified use

> If all you have is a hammer, everything looks like a nail.

This is the problem that haunts many novices who have just familiarized themselves with patterns. Having learned about patterns, they try to apply them everywhere, even in situations where simpler code would do just fine.