Cyc Core API Suite CHANGELOG
============================

For more information, view the [README](README.md) bundled with this release or visit the
[Cyc Developer Center](http://dev.cyc.com/api/core/). 

**Important note about _backwards-compatibility:_** It is expected that more release candidates will
follow. Until the final 1.0.0 release, it is expected that future release candidates will break
backwards compatibility.

1.0.0-rc3 - 2015-08-14
----------------------

The third release candidate of Cycorp's new Java API suite. 1.0.0-rc3 is _not backwards-compatible_
with earlier releases. Note that _ResearchCyc 4.0q_ and _EnterpriseCyc 1.7-preview_ require 
[server code patching](server-patching.md) for compatibility with the 1.0.0-rc3 release.

#### New: Support for upcoming OpenCyc release

Adds support for the planned upcoming release of **OpenCyc 5.0-preview**. Note that the OpenCyc 
server will not have support for advanced features such as QuerySearch or ProofViewJustification. 
Classes and methods which are not supported by OpenCyc will reflect this in their javadoc 
description and in their signature by declaring that they throw a 
`com.cyc.session.exception.OpenCycUnsupportedServerException`.

There are no plans to support previous versions of OpenCyc.

#### Improved: Core API Specification

* _Query API_ interfaces moved into CoreAPISpec Maven module.
* Adds the QueryFactory (`com.cyc.query.QueryFactory`) to facilitate query construction.
* Compatibility with different Cyc server editions and versions is now better-documented via the 
  `UnsupportedCycOperationException` and `OpenCycUnsupportedFeatureException` exceptions.

#### Other changes

* Speeds up KB API conversion of terms to KBObjects, which in turn speeds up
  `Query#getResultSet()#getKBObject()`. This resolves
  [issue 2](https://github.com/cycorp/CycCoreAPI/issues/2).
* Core API Suite can detect and apply missing SubL code patches to a Cyc server. This behavior is
  disabled by default, but is easily enabled via System properties. See
  [server-patching.md](server-patching.md) for details.
* Links to [Cyc Dev Center](http://dev.cyc.com/) have been modified to reflect improved URL scheme.
* Assorted bug fixes.


1.0.0-rc2 - 2015-04-07
----------------------

The second release candidate of Cycorp's new Java API suite for interacting with the Cyc inference
engine and knowledge base. Note that 1.0.0-rc2 is _not backwards-compatible_ with earlier releases.

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
and knowledge base. Note that 1.0.0-rc1 is _not backwards-compatible_ with earlier releases.

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


