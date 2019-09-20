# Notes on hyperbolic geometry

A bijective conformal map from the open unit disk to the open upper half plane:

    z -> i (1 + z) / (1 - z) 
    
The inverse of this is the Cayley transform:

    z -> (z - i) / (z + i)
    
which also maps the real line to the unit circle (∞ -> 1). 

The Mobius transformations preserving the upper half plane are just
those of the form

    z -> (az + b) / (cz + d),   a,b,c,d in R
    
So this is the group PSL(2, R) . Chaining the formulae above, we can
make it act on the unit disk by

    z -> ( (a - ic)i(1 + z) + (b - id)(1 - z) ) /
         ( (a + ic)i(1 + z) + (b + id)(1 - z) )
         
The group PSL2(R) is simple: discrete subgroups of it are 
Fuchsian groups: most memorably, the modular group 
    PSL(2, Z) = < S, T : S^2 = (ST) ^ 3 = 1 >
where S: z -> -1/z, T: z -> z + 1.

Note any group with a 2-element and a 3-element admits a
map into it from the modular group, giving an easy way to
construct homomorphic images (and so, game levels).

Simplicity of PSL2(R) implies there is no way to 
"separate out the rotations", i.e. 
    (express this precisely?) there is no compass :0
    
     





