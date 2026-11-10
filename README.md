# java-gradle

A small Java 21 project built with Gradle. It is a fixture repository of the `e2e-jvm` tests of the AI Orchestrator (test case TC-2206) and of the manual smoke tests. Its real JaCoCo report is the fixture `gradle-java.xml` of the `coverage-jacoco` module (TC-1501).

This file is for contributors who change the JVM tests or this fixture. Use [`fixtures/README.md`](../../README.md) for the overview of all fixtures. The commands to build and test are in the section "Commands for the repository profile".

## What is in it

- One service class, `PricingService` (`src/main/java/com/example/pricing/`), and one enum, `CustomerTier`. The service keeps no state and calls no other system.
- JUnit 5 unit tests in `src/test/java/`.
- JaCoCo. `test` is finalized by `jacocoTestReport`, so every test run writes the XML report, also `./gradlew test`.
- Checkstyle with the small rule set in `config/checkstyle/checkstyle.xml`. Any violation fails the build (`maxWarnings = 0`).
- The Gradle wrapper (`gradlew`, `gradle/wrapper/`). The wrapper JAR is the unchanged file that `gradle wrapper` writes. The distribution is checked with its SHA-256 sum.

## Versions

Chosen on 2026-10-31. They are stable releases. The Java line has its own version line in the best-practices pack (TC-2102 checks this against `fixtures/technologies.json`).

| Tool | Version |
|---|---|
| Java (`options.release`) | 21 |
| Gradle (wrapper) | 9.8.2 |
| JUnit Jupiter | 5.14.4 (`junit-bom`) |
| JaCoCo | 0.8.16 |
| Checkstyle | 14.3.0 |

## Commands for the repository profile

All commands are argument lists without shell operators (D-27). Run them in the repository root.

```yaml
languages: [java]
buildTool: gradle
technologies:
  - { name: java, version: "21" }
commands:
  format: ""
  lint: ./gradlew --console=plain checkstyleMain checkstyleTest
  build: ./gradlew --console=plain assemble
  fastTest: ./gradlew --console=plain test
  fullCheck: ./gradlew --console=plain clean check
coverageReport: build/reports/jacoco/test/jacocoTestReport.xml
coverageFormat: jacoco
testResults: [build/test-results/test]
generatedPaths: [build/]
```

There is no formatter, so `format` is empty. `fullCheck` runs Checkstyle and the tests, and then the JaCoCo report (the test task is finalized by it). It passes on the committed state. The first run downloads the Gradle distribution and the dependencies (about 1 minute).

## Coverage of the committed state

The committed tests cover all of the code except the line that throws for a negative price in `totalCents` (line 31). The JaCoCo model is in `packages/modules/coverage-jacoco/test/fixtures/gradle-java.expected.json`.

## Variant for TC-2206: new code that is not covered

`fixtures/variants/java-gradle/` has two patches for the scenario `jvm-feature`. Apply them with `git apply` in the root of the repository.

1. `uncovered.diff` adds the method `shippingCents` to `PricingService` and no test. The build still passes, because there is no repository threshold. JaCoCo reports `shippingCents` with 0 of 3 lines covered (lines 49, 50 and 52). New code coverage is 0 %, so Gate 3 fails with a value under 90 %.
2. `covered-fix.diff` goes on top of the first patch. It adds one test for `shippingCents`. Then all 3 lines and both branches are covered, and the CRAP score of the method (complexity 2, coverage 100 %) is 2.

Both states were built with `./gradlew clean check` (Checkstyle, tests and JaCoCo pass).

## About the JaCoCo report of the `coverage-jacoco` fixture

`gradle-java.xml` is the file `build/reports/jacoco/test/jacocoTestReport.xml` of a clean `./gradlew clean check`. One change was made by hand: the host name in the `id` of the `<sessioninfo>` element was replaced with `fixture-host`. The reader ignores that element.

## Rules for people and agents

- Keep the project small. Do not add a dependency.
- Do not commit `build/` or `.gradle/`. They are in `.gitignore`.
