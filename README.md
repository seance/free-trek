# free-trek

A study of *Free monads* and their applications in testing service orchestration, with a *Star Trek* theme.

**Prerequisites**
- [SBT](http://www.scala-sbt.org/) 0.13.13+ recommended
- [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 1.8.0+ recommended

**Running**

Run `sbt test` in the root directory. Note that the test execution may be slow due to a [problem](https://thoeni.io/post/macos-sierra-java/) with Java on macOS Sierra.

## Motivation

The motivation for this exercise is in two observations &mdash;
- Service logic in contemporary software systems is often *integration* or *orchestration* logic
- Testing such logic is problematic using traditional means, such as mocks

Free monads offer a possible solution to these problems. This study exercises two implementations, one manual encoding using Typelevel [Cats](https://github.com/typelevel/cats) and a second encoding using the [Freestyle](http://frees.io/) framework.

But, let's dive in to the scenario and see how this works in practise!

## The Problem

**James T. Kirk** has a problem. The *USS Enterprise* has recently been fitted with a new *multitronic computer*, and while her subsystems are tried and true, the Captain is concerned whether the new computer will correctly *coordinate* those systems.

After all, each expedition into space can feature a number of potentially dangerous encounters! Not only that, but the computer needs to keep track of the remaining *dilithium* and coordinate warp speed accordingly.

We have been given a spec of the Enterprise's systems by **Montgomery Scott**, found in the `trek-domain` project. Now, we only need to write a highly testable system that will satisfy the intrepid Captain!

## The Plan

Since Functional Programming is clearly the way of the future, and we're already in the 24th century in *Star Trek*, we'll make use of Free monads. It must be a good choice, since it already sounds like something from the Star Trek universe!

We *could* use mocks and spies for the testing, but let's face it &mdash; the Captain doesn't like spies on his starship. Keeping mocks up-to-date is also tedious and error-prone.

Encoding the logic operations in Free *reifies* the logic as data. This data can then be conveniently tested against known fixtures.

There are two implementations:
- `trek-cats` A Cats-based manual Free encoding layered under a monadic planning façade
- `trek-frees` Freestyle based implementation with a `cats-mtl` planning façade

Let's take a look at the manual to see how the Enterprise's systems operate.

## USS Enterprise Operating Manual

*Note!* Before attempting to operate your *Constitution-class* starship, you must first turn the crank shaft to start the ignition!

### Warp Core

The heart of the ship, producing the energy for the other systems.
- Before making warp jumps, you need to convert dilithium crystals equal to the warp speed, or lower the warp factor (to zero if necessary)
- When operating in asteroid fields, you should engage the antimatter reaction assembly

### Warp Drive

The warp-capable drives that propel the starship in the vast void of space.
- To perform a warp jump, tune the warp field coil to desired warp speed
- When in empty space, you should take the opportunity to attenuate the plasma conduit
- Trudging through nebulae, it is important to disentangle the electro-plasma system

### Phasers

Powerful directed-energy beam weapons that operate on various frequencies.
- When encountering hostile space crabs, focus for high frequency optronics, without forgetting to purge the prefire chamber
- When operating near space-time anomalies (a common occurrence), it is wise to rewire the power cell matrix

### Deflector Shields

Highly polarized energetic distortion field emitters that protect the starship.
- When encountering hostile space crabs, go for parallel frequency modulation
- While negotiating asteroid fields, it is prudent to use anti-parallel frequency modulation instead
- Always decompress the graviton emitter when operating near space-time anomalies!
