# The Algorithm Design Manual - Steven S. Skiena

read book as: burble through, note takeaways from each page

# 1 Intro to Alg Design

algorithm: procedure for accomplishing specific (general, well-specified) task.
Fundamental distinction between:
- the problem (e.g. travelling salesman)
- an instance of a problem

canonical example: sorting an array
insertion sort: can sort the array in place, O(N^2) time (says NR), constant space 

Desirable properties for an algo: 
- (provably) correct
- efficient
- easy to implement

Note in industrial settings, many algorithms "seem to be good enough" and that's fine -
analysis of correctness/efficiency may not be a requirement, while "easy to implement"
may be the overriding concern:

- second best to a provably correct algorithm is a *heuristic* : something that kinda
seems to work, under certain conditions, maybe. There is a time and a place for these.

Here concentrate on correctness: In general this involves proving a (mini-)theorem.
Correct algorithms come with a proof of correctness. So:

- the concept of "correct algorithm" is intrinsically mathematical.

For some algorithms, it may be obvious (or trivial) that they're correct -
but this still requires rigorous argument.

# 1.1 Robot Arm Problem (aka TSP)

for the 'robot arm tour' (find optimal cycle of points on a circuit board,
equivalent to travelling salesman problem) it might seem obvious to
choose the nearest neighbour, but this is not in fact clearly optimal.
Counterexample: equally spaced points on a line, starting in the middle

or: repeatedly connect closest pair of points?
but e.g. close-packed rows of equally spaced points breaks this.

Is there a correct algo at all? Could enumerate ALL routes? Correct, but too slow, O(N!)

# 1.2 Job selection problem
 
Actor has to select from movie projects which require blocking out
an interval of his time. Suppose each pays the same fee PER FILM: so
select largest possible number of nonoverlapping intervals. 1-D knapsack problem.

Could repeatedly: take job with earliest start date ("greedy" approach?) 

or: shortest job first? Not hard to cook up devilish examples that make each of
these very suboptimal.

But you can convince yourself that this always gives the optimal schedule:

- Repeatedly pick the job that finishes first.

Because at each stage, there may be other jobs overlapping this one, but
they are all mutually exclusive, and picking this one is clearly best.
(Exercise: formalize as a proof)

# 1.3 Reasoning about correctness

Required: precisely expressed algorithm, and something like a mathematical proof:
Hypothesis, conclusion, chain of reasoning.
Not getting too formal here. Aim for clarity in pseudocode: expressing the essential
idea, but without ambiguity.

Important and honourable technique: Simplify the problem until you CAN find an
optimal (correct, efficient) solution to it.

In demonstrating incorrectness: find simple counterexamples.

In structuring algorithms and correctness proofs: Induction & recursion are your friend.

# 1.4 Modelling the problem

Given a real life problem, find a simplified *model* of it expressed in terms of
procedural operations on *fundamental abstract structures* such as:

combinatorial objects: permutations, subsets, trees, graphs, strings
or in some geometric space: points, regions, polygons 

Modelling: reduce your problem to a simpler, more abstract question expressed in
terms of these structures. Sometimes this can simplify and clarify: sometimes it
throws away something essential: sometimes it's worth questioning assumptions to 
simplify the problem enough that you can solve it, thereby giving insight.
Don't be too quick to regard your problem as unique and special.

Recursion is a useful tool here: define/specify something in terms of a simpler
version of itself. For proving correctness, this leads to the idea of *inductive proofs*.

## 1.5 War stories

(Simplified, punched-up versions of) things that really happened.
Instructive examples of correct and incorrect modelling. For example:

## 1.6 Lotto problem

in the lottery: each ticket has 6 numbers each in the range 1-44 inclusive.

But a (psychic) client can visualise 15 numbers out of 44 and be certain that
at least 4 of them will be on the winning ticket.

Problem is to find most efficient way to exploit this information.
Suppose there is a cash prize for picking >= 3 of the winning numbers on your ticket.
Then: construct smallest set of tickets we must buy in order to definitely win
at least one prize. "Can assume psychic is 100% correct" (apparently a true story).

Special case of a set covering problem. The 4 parameters are:
the size n of the candidate set (15)
number k of slots for numbers (6)
number j of psychically-promised correct numbers (4)
number l of matching numbers necessary to in a prize (3)

Given this problem offhand, author attempts to find a heuristic which will sorta
kinda find a reasonably optimal solution. 
This is ok for the lotto co's purposes - only have to be better than a competitor.

Initial breakdown:
- generate all k-subsets from candidate set: (n choose k)
- try to pick a small set of tickets s.t. we have covered each of the (n choose l)
possibly winning l-subsets of S.
- eep track of prize combinations already covered. Suggest bit vector
- search mechanism to decide what ticket to buy next
(exhaustive, for small sets? or simulated annealing?)

So here's the pseudocode:
LottoTicketSet(n, k, l)
    Initialize (n choose l)-length bitvector V to 0
    Initialize set L of bought tickets = {}
    While V not all 0:
        select k-subset T as the next ticket, add to L
        for each l-subset T' of T, set V[T'] = true
    Report L

A variant of this was submitted to the lotto co - 
sample solution with given params: 28 tickets.
But co retorts thay can do it with 5 tickets that cover everything twice over.

Incorrect modelling: you don't need to cover all winning combinations.
Should have worked through a small example.
But fixed by changing "which subsets we get credit for covering with a given set
of tickets". This shows fundamental approach was sound, at least.

(I'm left unclear what the insight is that leads to an efficient solution - it all
seems brute force. Not an illuminating example...)

## References

for algos: Corman, Kleinberg/Tardos, Manber
war stories: Jon Bentley's Programming Pearls

See also www.programming-challenges.com -
doesn't exist any more but there are topcoder, coderbyte, project euler, HackerRank

sample problem: given 25 horses, if you can race them 5 at a time, how many
races are required to identify the top 3 horses?
quick analysis: we obviously have to race every horse, so HAVE to do >= 5 races.
Say initial set of 5 covers all of them. Then race the top 5.
You now know the top horse. The overall #2 must then be raced against...

