# Mottoes vocab 

An attempt to set in stone the emerging vocabulary for the "functional mottoes" subproject.

We assume an 'alphabet' (set of letters) which in practice are just ordinary capital letters. 
Not foreseeing ever needing more than 26.

The inspiration is that in Scala, there's essentially only one way to define a method like:

    def rm[X, S]: (S => S => X) => S => X =
        ssx => s => ssx(s)(s)
        
Analyzed categorically, this defines multiplication on the state monad (or something) and the axioms of tensorial
strength can also be encoded this way, as well as the basic axioms that define categories. So it might be interesting
to have a program to enumerate mottoes in the hope of finding undiscovered axioms of category theory. More likely, there
is a boring theorem waiting to be proved by somebody who understands all this better than I do, saying that all
mottoes can be derived from some small finite subset of them. But then again there might be something out there beyond the monads.

Original presentation: https://github.com/fdilke/bewl/blob/master/notes/FunctionalMottoes.pdf         

But we first have to clarify what defines these kinds of type puzzles, what it means for them to have a unique solution, and
what other properties are required before we elevate them to the status of functional motto. So:

## Binary forms 

A binary form is either a letter, or "p => q" where p and q are previously defined binary forms.

But given a form like p => (q => r), I'd prefer to think of it (equivalently) as a function mapping the pair (p, q) to r.
So it seems better to deprecate these in favour of the following equivalent formulation. All forms will be multiary in what follows.

## Multiary forms

A multiary form is either a letter, or (p, q, ... y) => z where all of p, q, ..., y are multiary forms and z is a letter.

We call these either "basic multiary forms" (i.e. letters) or "compound multiary forms" (the other type).

## Unique solvability

Basic multiary forms are never uniquely solvable.

A compound multiary form (p, q, ..., y) => z is uniquely solvable if exactly one of p, q, ..., y has the property that we
can derive z from it, in the sense that either:

- it IS z

- it is (a, b, ..., d) => e where e IS z and we can derive all of a, b, ..., d uniquely from p, q, ..., y.

This isn't quite a precise definition of when one form can be uniquely derived from a finite sequence of others.

## Arguments and internal arguments

A compound multiary form (p, q, ..., y) => z has p, q, ..., y as arguments.

Its internal arguments are (recursively) defined to be its arguments p, q, ... y together with all of their internal arguments.

## Redundant arguments

When "solving" a form, there is a natural concept of which arguments (forms appearing internally as arguments at the top level) 
are used. If they all are, and if the form can be uniquely solved, we say the form can be uniquely solved without redundant
arguments. But even this isn't quite enough to make it a motto. Consider:

    (A => A) => A
    
This shouldn't be a motto because we can trivially find an A => A, namely the identity. Indeed A => A is itself a motto.    

## Canonicality

For purpose of enumeration, we're also not interested in mottoes that are alternate versions of existing ones, with letters reassigned. 

We therefore want to exclude B => B from consideration, given we already have A => A. So:

A form is canonical if when the letters in it are listed by order of appearance, the result is an initial segment of the alphabet. 

## What is a motto?

Emerging definition: a form is a motto if

- it is canonical

- it can be uniquely solved without redundant arguments

- it does not contain a motto as an internal argument

