# Inheritance Review

We are setting up our Cell structures very similarly. The largest difference was that Megan's group is having one,
concrete cell class that is closed to everything. She is dealing with the different rules by creating a State class
that has concrete implementations and can be created for each simulation. We are doing it slightly different in
that we are handling the rules as part of the concrete cell implementations which are subclasses of our
abstract cell superclass.

We are both nervous about Percolation, as it depends on connections between cells that are greater than
one cell away. We are in agreement that recursion is likely the best way to deal with that,but I am interested
in thinking on that further.