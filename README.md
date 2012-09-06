jarvar
======

A smaller Java, with an embeddable ScriptLanguage going Vertex

A git repo of http://jarvar.googlecode.com on 2012-08-12

The google code side has a wiki, and more docs.

===============================================================

A simple class library of few classes to get programs running.
A simple script language of few words. List and stack based.
Main Compiler class and SL language has not changed in a
long time, and now development is switching to IO.

Directions
==========

There is no IO written yet, and the intent is to go 3D intrinsic.
All 'print' should return a vertex group for display, play, save,
see, move and load. The fact there is no IO layer at present is
a bonus, not a problem. This involves expanding coloured strings to
be subclasses of a new crux class Vert.

Simple Known Optimizations Not Done Yet
=======================================
All found unfounds should be optimizable by reversing word list
and replacing the contained found within the container unfound.
Allow no spaces around [ and ].
