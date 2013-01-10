Overview
========

Measurements is a simple, dependency-free library for interacting with imperial and metric measurements.  The code for this library was originally created as part of my [Measurement Calculator](https://play.google.com/store/apps/details?id=com.arahlf.mc) Android application.

How To Use
========
I've included the packaged jar file in the root directory of the master branch.  You can also build the source code yourself (if you're into that kind of thing).

This library has no 3rd-party dependencies, so just add the jar file to your classpath and you're all set.

For more information, take a look at the [JavaDoc](http://arahlf.github.com/Measurements/).


Code Samples
========

When working with the ``Unit`` enum, it's helpful (from a clean-code perspective) to use static imports.

**Basic Arithmetic Operations:**

    Measurement augend = Measurement.create(3, Unit.FOOT);
    Measurement addend = Measurement.create(6, Unit.INCH);
    Measurement sum = augend.add(addend);
    
    sum.toString(); // "3.5ft"

**Scaling:**

    Measurement measurement = Measurement.create("9.748", Unit.MILLIMETER);
        
    measurement.scale(2).toString(); // "9.75mm"

**Equality:**

    Measurement foot = Measurement.create(1, Unit.FOOT);
    Measurement footInInches = Measurement.create(12, Unit.INCH);
    
    foot.equals(footInInches); // true

**Formatting:**

    // Decimals
    Measurement measurement = Measurement.create("37.567", Unit.CENTIMETER);
    MeasurementFormatter formatter = new DecimalMeasurementFormatter(1, Unit.MILLIMETER);
    
    formatter.format(measurement); // "~375.7mm"
    
    formatter = new DecimalMeasurementFormatter(1, Unit.CENTIMETER, Unit.MILLIMETER);
    
    formatter.format(measurement); // "~37cm 5.7mm"

    // Fractions
    Measurement measurement = Measurement.create("5.875", Unit.INCH);
    MeasurementFormatter formatter = new FractionMeasurementFormatter(8, Unit.INCH);
    
    formatter.format(measurement); // "5-7/8in"
