# Variable symmetry

idea is to have a continuously changing pattern which has different symmetries (over time)
represented by different subgroups of the dihedral group D_2n

start by building a lattice which records the ordering of all subgroups H <= D

Then whenever H <= K, we can move continuously from an H-symmetric pattern to a K-symm one:

    given S (a set of configurations in some space acted on by D) we transition SH -> SK by:
    write K as a disjoint union of cosets x_iH (with x_0 = 1) then transition from
    SH = V sH => V sK = V sx_iH
    by having each sh -> sx_i  as time goes from 0 to 1
    
Keep doing this with a series of zigzagging subgroups H_0 < H_1 > H_2 < ...

Start with lattice of subgroups. If we regard the cyclic group C_2n < D_2n of index 2, 
I would expect that every subgroup is either a cyclic subgroup =~= C_m of C_2n or
"dihedral" in that it's generated by one of these with a reflection and =~= some D_2m. 

have existing code to treat dihedral elements as permutations... but then
how to interpret them as linear transformations? better to store as (+/-1, k)
for a rotation by pi_k/n optionally followed by a reflection?

So plan to: Not actually use the GenerateGroup / Permutation stuff.

# Recap on dihedral groups

Symmetries of an n-gon ::= D(2n) ( Geometers call it D(n) )
= < F, T: T ^ F = T ^ -1, T ^ n = F ^ 2 = 1 >
generated by a roTation T and a reFlection F

so then, rather degenerately, D(2) = C(2) and D(4) is the Klein 4 group

OK, I have an implementation of this. Next, to calculate subgroups.
Seems best to first move the permutation stuff back in and make it work 
with this machinery - then I have more groups to test the subgroup-enumerator with
or maybe quickly redevelop the bare essentials - this code dates from 2014, can do better!

# notes for hack day:

What I did

    Started writing a program to generate snowflake-type patterns of continuously variable symmetry.
    The idea is that while you’re watching the pattern evolve, it will smoothly morph from having (say) reflection symmetry to 3-fold rotational symmetry, and back again.
    I worked out a formula to do this (based on subgroups of the dihedral group D_2n) and have been thinking about implementing it for a while
    
What I learnt

    My Scala is getting a bit rusty. In particular I had to remind myself how ScalaTest’s BeMatchers work
    The theory of the dihedral group isn’t quite as simple as it seems
    I need a subgroup-enumerating algorithm and therefore more groups to test it on
    Remastering my permutation code from 2014 improved it a LOT
