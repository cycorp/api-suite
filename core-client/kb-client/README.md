Cyc KB API
==========

A Java API for manipulating CycL-based Java objects, both for the purpose of 
making assertions and for retrieving terms and values via simple queries.

The API extracts the main Predicate Logic constructs in the Cyc KB as Java 
classes. A set of factory methods allow finding and creating terms and
assertions. These can be used to build sentences, to create new assertions or to
build queries.

For more information, visit the [Cyc Developer Center](http://dev.cyc.com/).

Requirements and Getting Started
--------------------------------

This project is one of several libraries which are intended to be built in 
conjunction. See the [CycCoreAPI](https://github.com/cycorp/CycCoreAPI) project 
to get started.

**Note:** It is _strongly recommended_ to only run the API test suites against a 
_fresh Cyc server instance_ dedicated to that purpose. The API tests may alter
a Cyc server's KB contents.

Installing the CycCoreAPI will install this library, but once you have done so,
you may rebuild this library independently. From the root of this library's
directory, run:

    mvn install

This will run the test suite, which will pop up a GUI panel asking for a Cyc 
server address. If you are running in a headless environment, or wish to 
specify the server at the command line, use the following instead:

    mvn install -Dcyc.session.server=[HOST_NAME]:[BASE_PORT]

For example:

    mvn install -Dcyc.session.server=localhost:3600

Alternately, if you wish to install without running unit tests:

    mvn install -DskipTests=true 

Further Documentation
---------------------

For the latest API documentation and news, or to ask questions or report issues,
visit the [Cyc Developer Center](http://dev.cyc.com/). Code samples may be
downloaded from the [CoreAPIUseCases](https://github.com/cycorp/CoreAPIUseCases)
project.

Contact
-------

For questions about the APIs or issues with using them, please visit the
[Cyc Dev Center issues page](http://dev.cyc.com/issues/).
