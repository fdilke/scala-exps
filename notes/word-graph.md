# Word graph

given a set of words, consider any 2 "adjacent"
if they have at least one letter in common.
This makes the set into a graph. e.g. US states

then: have players alternate choosing a word,
not chosen before, "adjacent" to the last chosen one

# Questions

When is a position P, or N? Presumably P (with no previous selection) is a (moderately deep?)
property of the graph
is this computable for US states, by brute force
i.e. without some insight? 

Would the whole game be better played
on the complement i.e. only allowed to
play next state with 0 letters in common?

Or, using geographical adjacency - then
perhaps easier to identify structure of
"P-graphs" where the first player (in a new
game, with no history) loses.

# answers

Without shortcuts, takes too long to compute.

Comparing words for having a letter in common is
nontrivial enough that it should really be cached...
in fact, we should pre-index the words.

Almost all states have an A in them.
In fact, the graph is almost complete.
Complete graphs can be computed by the
    parity of their order: even -> P. 

# suggestions

There are plausible alternative adjacency rules.
This argues for making the adj criterion
pluggable, at least - so we can experiment
with different games.

Make type pluggable too.
Add an indexing scheme - so we index
each new T as it arrives, then cache
the results of adj tests on the indices.

When testing optimization, 
use a different word-set which is just big
enough to be a challenge: European countries? 

Given complete graphs can be computed easily...
Idea for optimization: if at any
point the graph is complete, apply parity rule.
But, best combined with:
remove any nodes we can't possibly
ever connect to - and that is HARD to
compute efficiently.

