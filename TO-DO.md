# Things to add

## cleaning up tests

Get rid of asserts. Import should-matchers everywhere

## Scala
Work through Odersky's "Scala levels"
http://www.scala-lang.org/old/node/8610.html

## groups

? Calculate all the subgroups
? Arrange them in a structure so we can generate a sequence of upward and downward 'segues'
? Precalculate coset representatives
? have them act on vectors
? have them act on 'transformables' e.g. a set of lines and circles
? generate the dihedral group of order 4
? have a concept of: convex combination in a segue acting on 'transformables'

fix Titanic and other classes that use assertResult. Expunge it

## Databases

HyperSQL / Slick

## Bewl
Add more 'generic topos' tests
Add signatures and algebras over them
Add algebraic laws (so we can define groups, rings, commutativity etc)
Add constructions like: centre of a group, automorphisms of an algebra
Are standardized exponentials a good idea?
Expunge use of * (added TODOs)
FiniteSets should use traversables (having made sure these are sensible) and functions

## Monads

Experiments with classes that support map, flatMap, filter
Set up a machinery of operators, double exponentials, etc to express:
- given a set and a bunch of operators on it, can construct the double-exp monad
- this is a monad (has left/right identity, associativity)
Bonus points for:
- given a monad, express it canonically as a double-exp one.


