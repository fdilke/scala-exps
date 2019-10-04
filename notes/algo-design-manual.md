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

 
