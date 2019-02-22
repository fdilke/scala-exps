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
 
# initial enumeration

Seems to work. The concatenation operator :: is handy for multiary forms, and
suggests that the binary forms are redundant - it's fine to just work with multiary
ones across the board.

But can't test this form for unique solvability:

    (((A) >> A) >> A) >> A
    
This causes some deeper type of infinite descent I should be testing for??
Also try to unify the two versions of canUniquelySolveXxx,
and use DoEnumerateForms to output the results.    

# fixing the solver

Refactored so 2 submethods are unified.
Maybe need more sophisticated heuristic for detecting infinite descent?
Have to guard against this scenario:

args/tgt/basic/innerArgs/innerTgt = (((A) >> A) >> A)/A/A/WrappedArray((A) >> A)/A
args/tgt/basic/innerArgs/innerTgt = (((A) >> A) >> A, A)/A/A/WrappedArray((A) >> A)/A
args/tgt/basic/innerArgs/innerTgt = (((A) >> A) >> A, A, A)/A/A/WrappedArray((A) >> A)/A

given <args, tgt> as inputs, end up reinvoking with <args :+ A,...>

If we're about to invoke a canUniquelySolve woth same args and a not-strictly-simpler target...
should we give up? Or abandon the whole thing?
Try: Just give up at this point. It has to be simpler.
So maybe don't need the "abandon whole search" mechanism?? <<== Probably this is correct

Seems to be ok to just give up. But the logic is too opaque - not clear enough what's going on. So:

- the difficult case is cUS(args, basic)

- for this to be true, there must be a unique arg for which either:

- it's a basic arg and is the target, or:
    
- the arg is innerArgs => innerTgt and for each inner arg: 

- we can uniquely derive the inner arg from what we already have, except:

- shouldn't even try under certain conditions.

Now, what are those? Should we accumulate a list of things we're in the process of trying to construct?
Should it be:

    canUniquelySolve(args, tgt, unreachableGoals)
    
and then if at any point we're trying to reach an unreachable goal, should just give up?
Prefer the idea of: making it false
But might occasionally be valid to set a more complex goal than the current target.

Should instead make all this more object-oriented - 
have a Seeker object which starts off with a list of args, tries to construct other stuff?
And keeps a list of stuff it knows is out of reach?

This would just be a relatively trivial refactoring of current scheme. Not useful.
Work with an "unreachable" list. Like this:

- if at any point we were about to look for something on the list of unreachables, give up.
- whenever you launch a new search, add the current target to the list of unreachables.

and again, NEVER abandon the search. This is declared not a useful concept. Maybe even get rid of the mechanism.

Don't yet need to spell out more precisely the possibly vague idea of "uniquely solvable".
We know there will be other criteria for a motto, because e.g. you never want to have a duplicate arg in any
subform.
     
And now, logically: if someone gives you an ((A) >> A) >> A, you can produce an A. 
Will have to spell out why this isn't a motto. Maybe should say more generally:

(MOTTO > A) > A   doesn't count as a motto.

This is of course just M[MOTTO] where M is a generic strong monad. So...

Better definition of a motto (categorical): an object in the topos for which there's a canonical global object.
Example: X ^ X. So if M is a motto, are we never interested in T[M] as a motto, where T is a strong monad?

BTW given M, find the unique 0 -> M, apply T and get a canonical 1 -> T[M]. Assuming T(0) is always 1??

If T = β_H then T(0) = (0 > H) > H = 1 > H = H. This is all presumably still true for H an algebra.

try to better understand: (1) why is there always a canonical global object for T[M] when M is a motto?
(2) why doesn't this count as a motto / new construction?

Note the forms are in fact just elements in the free magma <alphabet, ^> used as abstract
specifications. So for some of these there's a canonical global object, others, not.

# next refinement

Enumerate a bunch, see degenerate cases I want to exclude.
For example 'redundant' forms like "(A,B) >> A" which don't use all their arguments.

Recast code inside a special "motto finder" class which encapsulates all this. Will be like the Search
considered above, except it keeps track of more. Do as new code, using previous 'solver' as a template

At any point we have: a list of args, some of which may have been used, a target, and a list of unreachables.

# Lean and mean, or...?

Do we even need the concatenation operator for multiary forms, or is it essentially a binary thing / only useful for enumeration?
Should the forms be able to do analysis of their own structure?
If not, maybe build FormSolver code into CompoundMultiaryForm (where it looks like it belongs) and deprecate old solver code?

# What is a motto?

We're not entirely sure, but:
- a motto should be uniquely solvable with no redundant args
- a motto should not include another motto as an internal argument

# To do

- add concept of canonicity for multiary forms 

- in sanity checks, make sure enumerated forms are canonical

- Make sure new solver's concept of "unique solvability" (with optional redundant args) is consistent with previous code -
have a HighLevelSanityTest to ensure this

- Add a method to enumerate internal arguments

- Add tests that we correctly calculate what internal args are used when solving a form.

- Notes to firm up vocab?

- Expedient demise of the binary forms?



