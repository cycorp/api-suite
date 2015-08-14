Cyc Core API Specification
==========================

The set of interfaces which define the [Cyc Core API Suite](https://github.com/cycorp/CycCoreAPI).
This API is implemented across several modules within the Core API Suite.

For more information, visit the [Cyc Developer Center](http://dev.cyc.com/).

Requirements and Getting Started
--------------------------------

You normally should not need to build this project on its own, as it is one of several libraries 
which are intended to be built in conjunction. See the 
[CycCoreAPI](https://github.com/cycorp/CycCoreAPI) project to get started.

Installing the CycCoreAPI will install this library, but this library is simple to build 
independently, as it is purely an API specification. It deliberately has no dependencies on any 
other projects or resources within the CycCoreAPI hierarchy, and it does not inherit from the 
CycCoreAPI parent pom. To build it independently, run the following from the root of this project's 
directory:

    mvn install

This project does not contain a test suite.

Further Documentation
---------------------

For the latest API documentation and news, or to ask questions or report issues, visit the 
[Cyc Developer Center](http://dev.cyc.com/). Code samples may be downloaded from the 
[CoreAPIUseCases](https://github.com/cycorp/CoreAPIUseCases) project.

Contact
-------

For questions about the APIs or issues with using them, please visit the 
[Cyc Dev Center issues page](http://dev.cyc.com/issues/).
