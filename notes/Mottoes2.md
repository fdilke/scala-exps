# Second run at Functional Mottoes

A form is either <letter>, or <form> => <form>.

Idea is that the forms should be at least partially canonical

So call a form "sorted" if letters appear in sorted order, i.e. not "C => A"

and "m-sorted" if it could be the B in a sorted form A => B where A uses
precisely the first m letters 

Then central idea is to have a function enum(m, n) that does this:

    enumerate all m-sorted forms of length n

# A recursion problem

Why does this cause an infinite descent:

    A.from(A from A, A) i.e.
    ((A => A) => A) => A

add mechanism so if function is about to invoke itself with the same args, give up

but now: it thinks this is uniquely solvable, when it's not because we could be 
(undesirably) applying the A => A to the A

so when we detect infinite descent, should in fact give up at the top level, 
since here we know there isn't a unique solution.

so instead of x.count { ... } do:
loop over x
    if find infinite descent, give up

Why does this already seem unmanageable? Too many arbitrary special cases / tricky logic

Why not a formalism that does:

    UniqueSolution(Seq[A]) { a =>
    }
    
and then that can throw an InfiniteDescentException to abort the whole thing.    
    
# Properties of forms

Sortedness and unique solvability is only the start.

We're not interested in forms like "(C => A) => (A => A)" where the C is redundant.
Or even "(A => A) => (A => A)" where the A => A is.

So have a solving algorithm that keeps track of what inputs it's used in the solution?
 

