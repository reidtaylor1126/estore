# E-Store: Playing Cards

An online E-store system built in Java 8=>11 and Angular.js. This was a team project for an introductory software design course in my first year of college, but my parts of it should be a decent representation of my coding style in a somewhat large, formal Java project.
  
## Team

- Ryan Yocum
- James McGuire
- Clay Rankin
- Reid Taylor
- Nate Appleby


## Prerequisites

- Java 8=>11 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven
- Node.js


## How to run it

1. Clone the repository and go to the root directory.
2. Execute `mvn compile exec:java`
3. Open in your browser `http://localhost:8080/`

## Known bugs and disclaimers
(It may be the case that your implementation is not perfect.)

Document any known bug or nuisance.
If any shortcomings, make clear what these are and where they are located.

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## License

MIT License

See LICENSE for details.
