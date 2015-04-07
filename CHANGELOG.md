Cyc Core API Suite CHANGELOG
============================

For more information, visit the [Cyc Developer Center](http://dev.cyc.com/).


1.0.0-rc2 - 2015-04-07
----------------------

The second release candidate of Cycorp's new Java API suite for interacting with the Cyc inference 
engine and knowledge base.

For more information, view the `README.md` bundled with this release or visit the 
[Cyc Developer Center](http://dev.cyc.com/cyc-api/). Note that 1.0.0-rc2 is 
_not backwards-compatible_ with earlier releases.

#### New: Core API Suite Bundle

Adds the Core API Suite Bundle module (artifact `cyc-core-suite` in the `CoreAPISuite` subdir) which
packages all API interfaces and implementation classes. Details:

* Produces a single `cyc-core-suite` artifact which can be specified instead of individually 
  specifying cyc-session, cyc-kb, etc.
* Builds a single jar (in maven parlance, an "uberjar") which contains all of its dependencies. 
  Useful for some environments where it is complicated to pull binaries down from the Internet.

#### New: Core API Specification

Adds the Core API Specification module (artifact `cyc-core-api` in the `CoreAPISpec` subdir) to 
consolidate and clarify the interfaces which define the Core API Suite. Details:

* _KB API_ interfaces moved into CoreAPISpec.
* _Session API_ interfaces moved into CoreAPISpec.

#### Other changes

* Fixes tests which were failing against EnterpriseCyc.
* Changes parent pom artifactId from `cyc-core-api` to the more accurate `cyc-core-parent`.
* KB API test suite should no longer seem to hang per 
  [issue 1](https://github.com/cycorp/CycCoreAPI/issues/1).
* Updates developer email address in pom files to one which people can actually send mail to.
* Assorted updates in pom files to name and description elements.


1.0.0-rc1 - 2015-01-04
----------------------

First release candidate of Cycorp's new Java API suite for interacting with the Cyc inference engine
and knowledge base. 

For more information, view the `README.md` bundled with this release or visit the 
[Cyc Developer Center](http://dev.cyc.com/cyc-api/). Note that 1.0.0-rc1 is 
_not backwards-compatible_ with earlier releases.

Details:

* Updates the **KB API** (`1.0.0-rc1`), which streamlines the manipulation of CycL-based Java 
  objects.
* Updates the **Query API** (`1.0.0-rc1`), which streamlines asking queries and handling answers.
* Introduces the **Session API** (`1.0.0-rc1`), which manages configurations and connections to 
  Cyc servers.
* Replaces the Base API with the **Base Client** (`1.0.0-rc1`), a Java client for connecting to a 
  Cyc server and handling HL data.
  _Note that the Base Client is subject to frequent change, and so is not supported for external 
  developers._


1.0.0-Preview - 2014-07-09
--------------------------

Initial limited preview release! This is a new Java API suite to manipulate and query the Cyc KB.

Details:

* Introduces the **KB API**, which streamlines the manipulation of CycL-based Java objects.
* Introduces the **Query API**, which streamlines asking queries and handling answers.
* Introduces the **Base API**, the successor to the OpenCyc API.


