Cyc Core API Specification
==========================
**Latest release:** [![Maven Central](https://img.shields.io/maven-central/v/com.cyc/cyc-core-api.svg)](https://github.com/cycorp/api-suite/releases/)  
**License:** [Apache 2.0](LICENSE)  
**Changes:** See the [CHANGELOG](CHANGELOG.md)  
**Documentation:** [Cyc Developer Center](http://dev.cyc.com/api/core/)  

The Cyc Core API is the core set of Java APIs for interacting with the Cyc inference engine and 
knowledge base. It consists of three interrelated APIs:

* **KB API** (`com.cyc.kb`): Streamlines the lookup and creation of terms and assertions in the Cyc
  knowledge base.
* **Query API** (`com.cyc.query`): Provides tools for asking arbitrarily complex questions of a Cyc
  server, and processing the answers.
* **Session API** (`com.cyc.session`): Manages configurations and connections to Cyc servers.

The reference implementation of the Core API may be found in the 
[`api-clients`](https://github.com/cycorp/api-clients) repository, in the `core-client` directory.
For further details about the Core Client, see the project's README.


Requirements
------------

### Java

* Java 8 or greater to run, `JDK 1.8` or greater to build.
* [Apache Maven](http://maven.apache.org/) version `3.2` or higher to build the sources. If you are
  new to Maven, you may wish to view the [quick start](http://maven.apache.org/run-maven/index.html).

The APIs may be used without Maven via the `cyc-core-api-1.1.1-jar-with-dependencies.jar`.
See [Standalone Bundle](#standalone-bundle), below.

### Cyc Server

The following Cyc server releases are supported:

* **ResearchCyc 4.0q** or higher. Requires server code patching (see below).
* **EnterpriseCyc 1.7-preview** or higher. Requires server code patching.

For inquiries about obtaining a suitable version of Cyc, please visit the
[Cyc Dev Center downloads page](http://dev.cyc.com/downloads/).


### Server Code Patching

As of version 1.0.0-rc3, the Core Client implementation requires SubL code patches which are not 
present in _ResearchCyc 4.0q_ or _EnterpriseCyc 1.7-preview_. These patches can be applied manually,
or they can be automatically applied by the API bundle itself. For details, see `server-patching.md`
in the `core-client` project.


Downloading
-----------

### Apache Maven

To use the Cyc Core APIs in a Maven project, add the following dependency to your pom.xml:

    <dependency>
      <groupId>com.cyc</groupId>
      <artifactId>cyc-core-api</artifactId>
      <version>1.1.1</version>
    </dependency>

You'll normally want to also include the Core Client reference implementation:

    <dependency>
      <groupId>com.cyc</groupId>
      <artifactId>cyc-core-client-impl</artifactId>
      <version>1.1.1</version>
      <scope>runtime</scope>
    </dependency>

The Core APIs use the [SLF4J](http://www.slf4j.org/) logging API. They do not include a logging
implementation, but instead allow you to specify your own. For example, to use
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

For more details about logging, see the [Cyc API logging HOWTO](http://dev.cyc.com/api/faq/api-logging.html).


### Standalone Bundle

If you're not using Apache Maven, or can't allow Maven to retrieve dependencies from the Internet,
you may download a `core-client` standalone bundle containing:

* A standalone jar-with-dependencies for the Core API specification.
* A standalone jar-with-dependencies for the Core Client implementation.
* A test suite. (Requires Maven to compile.)
* Source code. (Requires Maven to compile.)
* Example code. (Requires Maven to compile.)

The bundle is available from the [releases page](https://github.com/cycorp/api-suite/releases) as a
tar or zip file. See the README in the bundle for details.


Building the Sources
--------------------

Within the `core-api` project, issue the following command:

    mvn install

There are currently no tests included. 

Of course, you will need an implementation in order to use the Core APIs. You may wish to build the
`core-client` project; see that project's README for details.


Further Documentation
---------------------

For the latest API documentation and news, or to ask questions, visit the
[Cyc Developer Center](http://dev.cyc.com/).

Code samples may be downloaded from the Cyc Core API Use Cases project in the 
[`example-code`](https://github.com/cycorp/example-code) repository.


Contact
-------

Issues may be reported via the [Cycorp issue tracker](http://dev.cyc.com/issues/).

For questions about the APIs or general issues with using them, please visit the
[Cyc Dev Center](http://dev.cyc.com/issues/).
