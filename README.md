Cyc Core API Suite
==================

The core Java APIs for interacting with the Cyc inference engine and knowledge 
base. This project includes the following:

* **Core API Specification** - Contains the interfaces which define the Core API Suite.
* **Core API Suite Bundle** - Packages all the other Core API modules into a single artifact.
* **KB API** - Streamlines the lookup and creation of terms and assertions in the Cyc knowledge 
  base.
* **Query API** - Tools for asking arbitrarily complex questions of a Cyc server, and dealing with 
  the answers.
* **Session API** - Manages configurations and connections to Cyc servers.
* **Base Client** - Client for connecting to a Cyc server and handling HL data;
  _not supported for external developers_.

For more information, visit the [Cyc Developer Center](http://dev.cyc.com/).


Requirements
------------

### Java

* `JDK 1.7` or greater to build; code can be run on Java 6.
* [Apache Maven](http://maven.apache.org/) version `3.2` or higher to build the sources. If you are
  new to Maven, you may wish to view the [quick start](http://maven.apache.org/run-maven/index.html).

The APIs may be used without Maven via the `cyc-core-suite-1.0.0-rc2-jar-with-dependencies.jar`.
See [Standalone Bundle](#Standalone Bundle), below.

### Cyc Server

The following Cyc server releases are supported:

* **ResearchCyc 4.0q** or higher.
* **EnterpriseCyc 1.7-preview** or higher.

The Core API Suite is _not_ presently compatible with any current release of **OpenCyc.**

For inquiries about obtaining a suitable version of ResearchCyc or EnterpriseCyc, please visit the
[Cyc Dev Center download page](http://dev.cyc.com/cyc-api/download.html).


Downloading
-----------

### Apache Maven

To use the Cyc Core APIs from a Maven project, add the following dependency to your pom.xml:

    <dependency>
      <groupId>com.cyc</groupId>
      <artifactId>cyc-core-suite</artifactId>
      <version>1.0.0-rc2</version>
    </dependency>

The Core APIs use [SLF4J](http://www.slf4j.org/) for logging, so you may also wish to specify a
logging implementation. E.g., for [Log4J](http://logging.apache.org/log4j/1.2/):

    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.7</version>
    </dependency>
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>
  </dependencies>

### Standalone Bundle

If you're not using Apache Maven, or can't allow Maven to retrieve dependencies from the Internet, 
you may download this release in a standalone bundle containing:

* A compiled `cyc-core-suite-1.0.0-rc2-jar-with-dependencies.jar`
* A `CoreAPITests` project containing the source code for all of the unit tests from the Core API 
  Suite, and the dependencies necessary to run them. This project may be run in Maven or as a native
  Eclipse project.
* The CycCoreAPI source code (requires Maven to compile)
* The CoreAPIUseCases source code (requires Maven to compile)

The bundle is available from the 
[releases page](https://github.com/cycorp/CycCoreAPI/releases/tag/v1.0.0-rc2) as a tar or zip file:

* cyc-core-suite-standalone-1.0.0-rc2.tar.gz
* cyc-core-suite-standalone-1.0.0-rc2.zip


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
[Cyc Dev Center](http://dev.cyc.com/cyc-api/issues.html).
