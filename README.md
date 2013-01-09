Overview
========

Measurements is a simple, dependency-free library for interacting with imperial and metric measurements.  The code for this library was originally created as part of my [Measurement Calculator](https://play.google.com/store/apps/details?id=com.arahlf.mc) Android application.

How To Use
========
For convienence, I've included the packaged jar file in the root directory of the master branch.  You can also build the source code yourself (if you're into that kind of thing).

This library has no 3rd-party dependencies, so just add the jar file to your classpath and you're all set.


Code Samples
========



Basic Arithmetic Operations:

    Measurement augend = Measurement.create(3, Unit.FOOT);
    Measurement addend = Measurement.create(6, Unit.INCH);
    Measurement sum = augend.add(addend);
    
    sum.toString(); // "3.5ft"
