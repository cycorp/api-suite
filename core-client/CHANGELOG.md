Cyc Core Client CHANGELOG
=========================

For more information, view the [README](README.md) bundled with this release or visit the
[Cyc Developer Center](http://dev.cyc.com/api/core/).

**Important note about _backwards-compatibility:_** It is expected that more release candidates will
follow. Until the final 1.0.0 release, it is expected that future release candidates will break
backwards compatibility.


1.0.0-rc5.2 - 2016-01-27
------------------------

* Fixes a ParaphraserFactory-related StackOverflowError in the Base Client 
  (CycClient#commonInitialization()) which occurred when NL API was on the classpath.
* The warning message which is logged when the NL API is missing from the classpath has been toned 
  down & clarified a bit, and is only logged when the user actually attempts to do something 
  NL-related (i.e., when the ParaphraserFactory first attempts to load a Paraphraser).


1.0.0-rc5.1 - 2016-01-18
------------------------

Fixes several bugs in the Core Client implementation:

* QueryImpl#getAnswerSentence() not substituting bindings.
* CycListParser mangling very big integers.
* LegacyCycClientManager#setCurrentSession(CycServerAddress) being ignored.
* UnmodifiableCycList modified by java.util.Collections#sort() under Java 8.

Additionally, in keeping with Google Java Style, references to "ELMt" have been changed to "ElMt".


1.0.0-rc5 - 2015-12-18
----------------------

The fifth release candidate of Cycorp's Java API suite, and the first in which the Core API 
specification and the Core Client implementation are packaged as wholly separate artifacts.

1.0.0-rc5 is _not backwards-compatible_ with earlier API releases. Note that _ResearchCyc 4.0q_ and
_EnterpriseCyc 1.7-preview_ require [server code patching](server-patching.md) for compatibility 
with the 1.0.0-rc5 release.

#### API & implementation division

There is now a clean division between the Core APIs and their reference implementation; they have
been split into two artifacts:

* The implementation-independent Core API specification: `core-api-spec`
* The Core Client reference implementation: `cyc-core-client-impl`

See the CHANGELOG in the Cyc Core API (v1.0.0-rc5) for additional details.

### Other changes

* The Cyc Core API GitHub repository has been renamed and expanded into the 
  [Cyc Java API Suite](https://github.com/cycorp/api-suite) repository, of which `core-api-spec` and
  `core-client` are sub-projects.
* Reorganization of classes (especially exceptions) within the com.cyc.* package space.
* Implementation projects built on the Base Client have had their artifactIds renamed to incorporate
  the word "client". E.g., `cyc-core-suite` is now `cyc-core-client-impl`, `cyc-session` is now 
  `cyc-session-client`, etc.
* Project/module directories have been renamed to mirror their artifactIds, dropping the "cyc-" 
  prefix. E.g., the `cyc-session-client` project lives in the `session-client` directory.
* The `cyc-core-api-parent` and `cyc-core-client-parent` POMs now inherit from `cyc-api-parent`,
  which inherits from `cyc-default-config-parent`.
* Assorted bug fixes.
