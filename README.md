Cyc Core API Suite
==================

The core Java APIs for interacting with the Cyc inference engine and knowledge 
base. This package includes the following:

* **KB API** - Streamlines the lookup and creation of terms and assertions in
  the Cyc knowledge base.
* **Query API** - Tools for asking arbitrarily complex questions of a Cyc
  server, and dealing with the answers.
* **Session API** - Manages configurations and connections to Cyc servers.
* **Base Client** - Client for connecting to a Cyc server and handling HL data;
  _not supported for external developers_.

For more information, visit the [Cyc Developer Center](http://dev.cyc.com/).

Requirements
------------

* `JDK 1.7` or greater to build; code can be run on Java 6.
* An EnterpriseCyc or ResearchCyc server running at least system `10.152303`. 
  The current version of the Cyc API suite is _not_ compatible with the latest 
  4.0 OpenCyc release. For inquiries about obtaining a suitable version of
  EnterpriseCyc or ResearchCyc, visit the 
  [Cyc Dev Center download page](http://dev.cyc.com/cyc-api/download.html).
* [Apache Maven](http://maven.apache.org/), version `3.2` or
  higher. If you are new to Maven, you may wish to view the
  [quick start](http://maven.apache.org/run-maven/index.html).

Getting Started
---------------

In addition to this project, you will need to clone the following projects:

* [SessionAPI](https://github.com/cycorp/SessionAPI)
* [BaseClient](https://github.com/cycorp/BaseClient)
* [KBAPI](https://github.com/cycorp/KBAPI)
* [QueryAPI](https://github.com/cycorp/QueryAPI)

All five projects should reside at the same level, e.g.:

* [some dir]
    * BaseClient
    * CycCoreAPI
    * KBAPI
    * QueryAPI
    * SessionAPI

**Note:** It is _strongly recommended_ to only run the API test suites against a 
_fresh Cyc server instance_ dedicated to that purpose. The API tests may alter
a Cyc server's KB contents.

Within the CycCoreAPI project, issue the following command:

    mvn install

This will run the test suites for all of the sub-projects, and will pop up a 
GUI panel asking for a Cyc server address. If you are running in a headless 
environment, or wish to specify the server at the command line, use the 
following instead:

    mvn install -Dcyc.session.server=[SOME_HOST_NAME]:[SOME_BASE_PORT]

For example:

    mvn install -Dcyc.session.server=localhost:3600

Alternately, if you wish to install without running unit tests:

    mvn install -DskipTests=true 

Installing the `CycCoreAPI` project will install all of its sub-projects. Once 
you have done so, you can then re-build/re-install any sub-projects 
independently. See each project's `README.md` for details.

Further Documentation
---------------------

For the latest API documentation and news, or to ask questions or report issues,
visit the [Cyc Developer Center](http://dev.cyc.com/). Code samples may be
downloaded from the [CoreAPIUseCases](https://github.com/cycorp/CoreAPIUseCases)
project.

Contact
-------

For questions about the APIs or issues with using them, please visit the
[Cyc Dev Center issues page](http://dev.cyc.com/cyc-api/issues.html).