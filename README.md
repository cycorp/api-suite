Cyc Core API Suite
==================

**Latest release:** [1.0.0-rc3](https://github.com/cycorp/CycCoreAPI/releases)

**Current snapshot version:** 1.0.0-rc4-SNAPSHOT  
**License:** [Apache 2.0](LICENSE)  
**Changes:** See the [CHANGELOG](CHANGELOG.md)  
**Documentation:** [Cyc Developer Center](http://dev.cyc.com/api/core/)  


The Cyc Core API Suite is the core set of Java APIs for interacting with the Cyc inference engine 
and knowledge base. 

The _Core APIs_ consist of three interrelated APIs:

* **KB API** (`com.cyc.kb`): Streamlines the lookup and creation of terms and assertions in the Cyc
  knowledge base.
* **Query API** (`com.cyc.query`): Provides tools for asking arbitrarily complex questions of a Cyc 
  server, and processing the answers.
* **Session API** (`com.cyc.session`): Manages configurations and connections to Cyc servers.

The Core API _Suite_ includes those APIs and their reference implementations via the following 
modules:

* **Core API Specification** - The interfaces which define the Core API Suite.
* **Core API Suite Bundle** - Packages all the other Core API modules into a single artifact.
* **KB API Implementation** - Reference implementation of the KB API.
* **Query API Implementation** - Reference implementation of the Query API.
* **Session API Implementation** - Reference implementation of the Session API.
* **Base Client** - Client for connecting to a Cyc server and handling HL data;
  _not supported for external developers_.


Requirements
------------

### Java

* `JDK 1.7` or greater to build; code can be run on Java 6.
* [Apache Maven](http://maven.apache.org/) version `3.2` or higher to build the sources. If you are
  new to Maven, you may wish to view the [quick start](http://maven.apache.org/run-maven/index.html).

The APIs may be used without Maven via the `cyc-core-suite-1.0.0-rc3-jar-with-dependencies.jar`.
See [Standalone Bundle](#standalone-bundle), below.

### Cyc Server

The following Cyc server releases are supported:

* **ResearchCyc 4.0q** or higher. Requires [server code patching](server-patching.md).
* **EnterpriseCyc 1.7-preview** or higher. Requires [server code patching](server-patching.md).

The Core APIs also include support for the planned upcoming release of **OpenCyc 5.0-preview**,
although OpenCyc does not have support for advanced features such as QuerySearch or 
ProofViewJustification. Classes and methods which are not supported by OpenCyc will reflect this in 
their javadoc description and in their signature by declaring that they throw a 
`com.cyc.session.exception.OpenCycUnsupportedServerException`.

For inquiries about obtaining a suitable version of Cyc, please visit the
[Cyc Dev Center downloads page](http://dev.cyc.com/downloads/).


### Server Code Patching

As of version 1.0.0-rc3, the Core API Suite requires SubL code patches which are not present in
_ResearchCyc 4.0q_ or _EnterpriseCyc 1.7-preview_. These patches can be applied manually, or they
can be automatically applied by the API bundle itself.

See [server-patching.md](server-patching.md) for details.


Downloading
-----------

### Apache Maven

To use the Cyc Core APIs in a Maven project, add the following dependency to your pom.xml:

    <dependency>
      <groupId>com.cyc</groupId>
      <artifactId>cyc-core-suite</artifactId>
      <version>1.0.0-rc3</version>
    </dependency>

The Core APIs use the [SLF4J](http://www.slf4j.org/) logging API. This does not come with a logging
implementation, but instead allows you to specify your own. For example, to use
[Log4J 1.2](http://logging.apache.org/log4j/1.2/):

    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.12</version>
    </dependency>
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>

### Standalone Bundle

If you're not using Apache Maven, or can't allow Maven to retrieve dependencies from the Internet,
you may download this release in a standalone bundle containing:

* A compiled `cyc-core-suite-1.0.0-rc3-jar-with-dependencies.jar`
* A `CoreAPITests` project containing the source code for all of the unit tests from the Core API
  Suite, and the dependencies necessary to run them. This project may be run in Maven or as a native
  Eclipse project.
* The CycCoreAPI source code (requires Maven to compile)
* The CoreAPIUseCases source code (requires Maven to compile)

The bundle is available from the
[releases page](https://github.com/cycorp/CycCoreAPI/releases/tag/v1.0.0-rc3) as a tar or zip file:

* cyc-core-suite-standalone-1.0.0-rc3.tar.gz
* cyc-core-suite-standalone-1.0.0-rc3.zip

To use a standalone jar in a Maven project, use the "jar-with-dependencies" classifier when 
specifying the dependency:

    <dependency>
      <groupId>com.cyc</groupId>
      <artifactId>cyc-core-suite</artifactId>
      <version>1.0.0-rc3</version>
      <classifier>jar-with-dependencies</classifier>
    </dependency>


Building the Sources
--------------------

**Note:** Installing this project will run all of the API test suites. It is
_strongly recommended_ to only run the API test suites against a
_fresh Cyc server instance_ dedicated to that purpose. The API tests may alter
a Cyc server's KB contents.

Within the CycCoreAPI project, issue the following command:

    mvn install

This will run the test suites for all of the sub-projects, and will pop up a
GUI panel asking for a Cyc server address. If you are running in a headless
environment, or wish to specify the server at the command line, use the
following instead:

    mvn install -Dcyc.session.server=[HOST_NAME]:[BASE_PORT]

For example:

    mvn install -Dcyc.session.server=localhost:3600

Alternately, if you wish to install without running unit tests:

    mvn install -DskipTests=true

Installing the `CycCoreAPI` project will install all of its sub-projects. Once
you have done so, you can then re-build or re-install any sub-projects
independently. See each project's `README.md` for details.


Testing the Sources
-------------------

Building the sources (as described above) will automatically run the tests, but you may wish to run
the test suite independently.

**Note:**  It is _strongly recommended_ to only run the API test suites against a _fresh Cyc server
instance_ dedicated to that purpose. The API tests may alter a Cyc server's KB contents.

Within the CycCoreAPI project, issue the following command:

    mvn clean verify -Dcyc.session.server=[HOST_NAME]:[BASE_PORT]

E.g.:

    mvn clean verify -Dcyc.session.server=localhost:3600

Note that we're using Maven's `verify` phase instead of the `test` phase. This is required to
insure that integration tests are included. For more details, see
_Unit tests vs. integration tests_, below.

Test results will be displayed to the console, and saved in `[MODULE]/target/surefire-reports/`.
E.g., test results for the Session API will be saved in `SessionAPI/target/surefire-reports/`, test
results for the KB API will be saved in `KBAPI/target/surefire-reports/`, and so on.

Note that specifying the `clean` goal will cause Maven to delete the `target` directories, and any
existing reports and compiled unit tests, before re-compiling and running the test suites. If you do
not wish to delete the target directories, you can omit the `clean` goal. E.g.:

    mvn verify -Dcyc.session.server=localhost:3600


### Testing specific modules/sub-projects

To run only the tests for a particular module (sub-project), simply do the above from within that
module's root directory. For example, to run all tests for the Session API:

    cd SessionAPI
    mvn clean verify -Dcyc.session.server=[HOST_NAME]:[BASE_PORT]


### Running individual tests

You may run a single _unit_ test class from the command line by passing its simple class name as a
property:

    mvn clean test -Dtest=[NAME_OF_TEST] -Dcyc.session.server=[HOST_NAME]:[BASE_PORT]

These tests must be run from the root of the module (sub-project) in which the are contained. For
example, to run the Session API Implementation's `ConfigurationLoaderManagerTest`:

    cd SessionAPI
    mvn clean test -Dtest=ConfigurationLoaderManagerTest -Dcyc.session.server=localhost:3600

You may run a single unit test method:

    cd SessionAPI
    mvn clean test -Dtest=CycServerTest#testIsDefined -Dcyc.session.server=localhost:3600

You may also use patterns to run a number of unit tests from the same project:

    cd SessionAPI
    mvn clean test -Dtest=SessionManagerImplTest,Configuration*Test -Dcyc.session.server=localhost:3600

_Integration_ tests can also be run individually, but must be run during the `verify` phase:

    mvn verify -Dtest=CycSessionManagerIT -Dcyc.session.server=localhost:3600

    mvn verify -Dtest=CycSessionManagerIT#testSingletonAccessor -Dcyc.session.server=localhost:3600

Unit test can be run in either the `test` or `verify` phase, so either of these will work:

    mvn clean test -Dtest=ConfigurationLoaderManagerTest -Dcyc.session.server=localhost:3600

    mvn clean verify -Dtest=ConfigurationLoaderManagerTest -Dcyc.session.server=localhost:3600

However, because unit tests don't need the additional Maven integration testing overhead, it's
leanest to run them during the `test` phase, if possible.

For more information, see the [Maven SureFire Plugin examples](http://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html).


### Unit tests vs. integration tests

By default, Maven expects integration tests to be named `**/IT*.java`, `**/*IT.java`, or
`**/*ITCase.java`, and does not run them during the `test` phase. Instead, to allow for setting up
and tearing down the integration test environment, the Maven lifecycle has four phases for
integration testing: `pre-integration-test`, `integration-test`, `post-integration-test`, and
`verify`. To ensure that all of these phases run correctly, it's simplest to just call the `verify`
phase; any unit tests will be run, too.

For more information, see:

* [Maven Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference)
* [Maven Failsafe Plugin - Usage](http://maven.apache.org/surefire/maven-failsafe-plugin/usage.html)
* [Maven Failsafe Plugin - failsafe:integration-test](http://maven.apache.org/surefire/maven-failsafe-plugin/integration-test-mojo.html)


Further Documentation
---------------------

For the latest API documentation and news, or to ask questions, visit the
[Cyc Developer Center](http://dev.cyc.com/).

Code samples may be downloaded from the [CoreAPIUseCases](https://github.com/cycorp/CoreAPIUseCases)
project.


Contact
-------

Issues may be reported via this project's
[GitHub issue tracker](https://github.com/cycorp/CycCoreAPI/issues).

For questions about the APIs or general issues with using them, please visit the
[Cyc Dev Center](http://dev.cyc.com/issues/).


