# math

[![build status](https://github.com/mtresnik/math/actions/workflows/gradle.yml/badge.svg)](https://github.com/mtresnik/math/actions/workflows/gradle.yml/)
[![version](https://img.shields.io/badge/version-1.0.0-blue)](https://github.com/mtresnik/math/releases/tag/v1.0)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://github.com/mtresnik/math/blob/main/LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-green.svg?style=flat-square)](https://makeapullrequest.com)
<hr>

## Getting Started

### Maven

**~/.m2/settings.xml:**
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
    ...
  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>
    ...
  <servers>
    <server>
      <id>github</id>
      <username>GITHUB_USERNAME</username>
      <password>GITHUB_PAT</password>
    </server>
  </servers>
</settings>
```

**pom.xml:**
```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/mtresnik/math</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
...
<dependency>
    <groupId>com.resnik</groupId>
    <artifactId>math</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle (groovy)

**~/.gradle/gradle.properties:**
```groovy
gpr.user=GITHUB_USERNAME
gpr.token=GITHUB_PAT
```

**build.gradle:**
```groovy
repositories {
    ...
    maven {
        name="GitHubPackages"
        url= uri("https://maven.pkg.github.com/mtresnik/math")
        credentials {
            // Runner stored in env, else stored in ~/.gradle/gradle.properties
            username = System.getenv("USERNAME") ?: findProperty("gpr.user") ?: "<GITHUB_USERNAME>"
            password = System.getenv("TOKEN") ?: findProperty("gpr.token")
        }
    }
    ...
}

dependencies {
    ...
    compile "com.resnik:math:1.0.0"
    ...
}
```
<hr>

## linear

Common linear algebra functions and data structures, like vectors, matrices, tensors, quaternions.

Standard Double LinearAlgebra functions without overhead:
```kotlin
// Point Difference and Vector Scaling: p3 = t*(p2 - p1) + p1
val point1 = ArrayPoint(x1,y1,z1)
val point2 = ArrayPoint(x2,y2,z2)

val vector1 = point2 - point1
val vector1Norm = vector1.normalized()
val t = 0.75
val point3 = vector1Norm * t + point1 
```

Or you could paramaterize it using the included symbolic math package:
```kotlin
val point1 = Point(x1,y1,z1)
val x2 = Variable("x2")
val y2 = Sin(Variable.THETA) + 5.0
val z2 = SymbolicSyntaxAnalyzer().analyze("e^(pi * i)")
val point2 = Point(x2,y2,z2)

val vector1 = point2 - point1
val vector1Norm = vector1.normalized()
val t = 0.75
val point3 = vector1Norm * t + point1
```

## symbo

Adds symbolic math to kotlin. Uses **Abstract Syntax Trees**  to parse `Strings` into `Operations`.

```kotlin
val operation1 = SymbolicSyntaxAnalyzer().analyze("x + y + z")
val operation2 = Variable("exampleName") + 5.0

val result = operation / operation2
```

Also supports Complex-Number operations (sin(z), cos(z), exp(z), ln(z)) which were tedious before.

```kotlin 
val z1 = ComplexNumber(real=0.0, imaginary=2.0) // 2.0 * i
val z2 = 5.0.asComplex() // 5.0

val z3 = z1 + z2 // 5.0 + 2.0 * i
val z4 = z3.ln() // Complex Ln(z) -> z
```

The symbo package also supports `ComplexNumbers` first!

```kotlin 
val constant = Constant(ComplexNumber(3.0, -2.0)) // Constant(3.0 - 2.0*i)
val variable = Variable.Z

// Throwing in some "plug-in for Z code..."
val result = (constant * variable).evaluate(Variable.Z, 2.0)
val constantResult = result.toConstant()
println(constantResult) // 6.0 - 2.0*i
```

> **_NOTE:_**  In my previous implementation of this library in Java, there was support for general derivatives. This is an upcoming feature for the next version.

## optimize

Novel optimization techniques of variable ranges in both known and unknown domains.
* `RangedMinimizer` : Uses Immutable Dimensions and sub-Splines
* `BruteForceMinimizer` : Uses Immutable Dimensions and recursive Bounding Boxes
* `UnboundedMinimizer` : Uses Mutable Dimensions which rely on random directions, subsampled and sorted for the next iteration. Similar technique as the Bounding Box approach.
* `UnboundedGeneralMinimizer` : A minimizer for a general Dataset, where dimension mapping is implementation based.