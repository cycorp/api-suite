Cyc Core API Suite CHANGELOG
============================

For more information, visit the [Cyc Developer Center](http://dev.cyc.com/).

1.0.0-rc2-SNAPSHOT (Currently under development)
------------------
Adds **Core API Specification** module (`cyc-core-api` in CoreAPISpec) to consolidate and clarify 
the interfaces which define the Core API Suite.
* _KB API_ interfaces moved into CoreAPISpec.
* _Session API_ interfaces moved into CoreAPISpec.

Adds **Core API Suite Bundle** module (`cyc-core-suite` in CoreAPIBundle) which packages all API 
interfaces and implementation classes.
* Produces a single `cyc-core-suite` artifact which can be specified instead of individually 
  specifying cyc-session, cyc-kb, etc.
* Builds a single jar (in maven parlance, an "uberjar") which contains all of its dependencies. 
  Useful for some environments where it is complicated to pull binaries down from the Internet.

Other changes:
* Fixes tests which were failing against EnterpriseCyc.
* Changes parent pom artifactId from `cyc-core-api` to the more accurate `cyc-core-parent`.
* KB API test suite should no longer seem to hang per 
  [issue 1](https://github.com/cycorp/CycCoreAPI/issues/1).
* Updates developer email address in pom files to one which people can actually send mail to.
* Assorted updates in pom files to name and description elements.
* 1.0.0-rc2 is _not backwards-compatible_ with the 1.0.0-rc1 release.

1.0.0-rc1 - 2015-01-04
----------------------
* First public release candidate!
* Updates the **KB API** (`1.0.0-rc1`), which streamlines the manipulation of CycL-based Java 
  objects.
* Updates the **Query API** (`1.0.0-rc1`), which streamlines asking queries and handling answers.
* Introduces the **Session API** (`1.0.0-rc1`), which manages configurations and connections to 
  Cyc servers.
* Replaces the Base API with the **Base Client** (`1.0.0-rc1`), a Java client for connecting to a 
  Cyc server and handling HL data.
  _Note that the Base Client is subject to frequent change, and so is not supported for external 
  developers._
* 1.0.0-rc1 is _not backwards-compatible_ with the 1.0.0-Preview release.

1.0.0-Preview - 2014-07-09
--------------------------
* Initial limited preview release! This is a new Java API suite to manipulate and query the Cyc KB.
* Introduces the **KB API**, which streamlines the manipulation of CycL-based Java objects.
* Introduces the **Query API**, which streamlines asking queries and handling answers.
* Introduces the **Base API**, the successor to the OpenCyc API.


