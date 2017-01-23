# Tangent Groups

Given a field F, we can attempt to make the set 
(F + infinity) into an abelian group by defining

    a * b = (a + b) / (1 - ab)

When a = infinity, the right hand side is interpreted as -1/b.

This works (verify) if -1 isn't a square in F, which for finite fielfds GF(q)
is the case precisely if q = 3 mod 4 (verify).

What's the structure of this group of order q + 1?

More generally if F is a division ring, do we get a nonabelian version?

Can this be made into a functor from fields and places to abelian groups?

Alternative construction: Given a ring R, when does the 'arctangent formula'

    atan2(y1, x1) * atan2(y2, x2) = 
        atan2( y1x2 + y2x1, x1x2 - y1y2 )

show how to make RxR into a group?

Later note: Here's what's really going on here.
If F has no square root of -1, then x^2 + 1 is irreducible and the
quotient ring F[i] := F[x]/<x^2 + 1> is a field. Then the group described
above is just the quotient of the two groups of units, F[i]*/F*, via the
map that identifies formal ratios p/q (where not both p, q are 0) with the
image of ip+q mod F*.