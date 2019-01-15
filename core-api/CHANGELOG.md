Cyc Core API CHANGELOG
======================

For more information, view the [README](README.md) bundled with this release or visit the
[Cyc Developer Center](http://dev.cyc.com/api/core/).


1.2.2 - 2019-01-15
------------------

Ensure that queries run successfully for any value of Browsable.


1.2.1 - 2019-01-14
------------------

Added support for the Browsable query parameter.


1.2.0 - 2018-07-17
------------------

Additions to the QueryAnswers interface.


1.1.1 - 2018-01-12
------------------

Version number incremented to reflect core client implementation update.


1.1.0 - 2017-12-18
------------------

Javadoc corrections. References to cyc-core-client-impl updated to 1.1.0.


1.0.0 - 2017-12-12
------------------

1.0.0 release. Future 1.x releases will be backwards-compatible, per the
[Semantic Versioning 2.0.0](https://semver.org/) specification.


1.0.0-rc9.0 - 2017-12-12
------------------------

Consolidates and streamlines factory methods, in addition to other assorted improvements. It is 
_not backwards-compatible_ with earlier API releases.

This release contains numerous minor improvements and cleanup in preparation for 1.0.0. 


1.0.0-rc8.0 - 2017-12-11
------------------------

Consolidates and streamlines factory methods, in addition to other assorted improvements. It is 
_not backwards-compatible_ with earlier API releases.

#### Java 1.8

Now requires Java 8 or greater to run, and `JDK 1.8` or greater to build.

#### Core API Factories

This release primarily simplifies all of the many factories. Previously, we had a separate factory
for each object (Assertion -> AssertionFactory, KbTerm -> KbTermFactory, Query -> QueryFactory, 
etc.) which was a constant headache. Now, each API has a single entry point via a service-provider 
interface (marked by `com.cyc.CycApiEntryPoint`) for which implementations are loaded at runtime;
e.g., `KbApiService`, `QueryApiService`, `SessionApiService`. For convenience, we then provide 
static methods which trampoline to these factories.

There are now two recommended ways to create or retrieve an object: either call static methods on
`com.cyc.Cyc`, or on the relevant interface itself. E.g.:

* `Cyc.getKbTermService().findOrCreate("SomeTerm")`
* `Cyc.getQueryService().getQuery("SomeQueryId")`
* `KbTerm.findOrCreate("SomeTerm)`
* `Query.get("SomeQueryId")`

Additionally, commonly-used constants can now be found on `com.cyc.Cyc.Constants`. E.g.:

* `Cyc.Constants.INFERENCE_PSC`
* `Cyc.Constants.UV_MT`
* `Cyc.Constants.ISA`
* `Cyc.Constants.GENLS`
* `Cyc.Constants.TRUE_CYCL`
* `Cyc.Constants.FALSE_CYCL`

Improvements have also been added to facilitate more fluent API calls via method chaining, 
particularly for queries. Instance methods have been added, where appropriate, which trampoline to
factories. Query answers are returned in a QueryAnswers instance, which contains a number of 
convenience methods. A number of typical CycL variables (`ARG`, `ARG0`, `ARG1`, `ARG2`, `VAR`, 
`VAR0`, `VAR1`, `VAR2`, etc.) have been added to `com.cyc.Cyc.Constants` for static import. 

For example, here are two different ways to run a query which finds and prints all instances of the 
Collection `#$Dog`:

    import com.cyc.kb.KbCollection;
    import com.cyc.kb.Sentence;
    import com.cyc.query.Query;
    import static com.cyc.Cyc.Constants.ARG1;
    import static com.cyc.Cyc.Constants.INFERENCE_PSC;
    import static com.cyc.Cyc.Constants.ISA;
    ...
    
    Query.get("(isa ?VAR Dog)", "InferencePSC")
            .getAnswers()
            .getBindingsForOnlyVariable()
            .forEach(System.out::println);
    
    ...
    
    Sentence.get(ISA, ARG1, KbCollection.get("Dog"))
            .toQuery(INFERENCE_PSC)
            .getAnswers()
            .getBindingsForVariable(ARG1)
            .forEach(System.out::println);

#### KB API

* Adds static factory methods to KbObject interfaces. E.g., `KbTerm#findOrCreate(String)`, 
  `KbTerm#get(String)`, etc.
* Adds abstract base classes for implementing KbObjects per decorator pattern: `com.cyc.kb.wrapper`.
  There is only partial coverage for the KbObject hierarchy at the moment, but this will be fleshed
  out soon.

#### Query API

* Adds static factory methods to Query API interfaces. E.g., `Query#getQuery(String)`, 
  `ProofViewSpecification#get()`, `ProofView#getProofView(QueryAnswer, ProofViewSpecification)`.

#### Session API

* Adds static factory method to CycSession interface: `CycSession#getCurrentSession()`.
* `CycAddress` interface replaces `CycServerAddress` and `CycServer`, for which the nomenclature was
  too ambiguous.
* Add `CycAddress#getConcurrencyLevel()`, which represents the maximum # of simultaneous jobs that 
  can be delegated to a particular server.


1.0.0-rc7.0 - 2017-07-28
------------------------

Refactors a number of interfaces to improve clarity, particularly in the KB API. It is 
_not backwards-compatible_ with earlier API releases.

#### Java 1.7

Now requires Java 7 or greater to run, and `JDK 1.7` or greater to build.

#### KB API

A number of methods on `com.cyc.kb.KbObject` have been moved to subtypes to better reflect their
relevance within the type hierarchy, and some have been renamed for clarity or consistency. For
example:

    KbObject#addFact      -> KbPredicate#addFact
	
    KbObject#getValues    -> KbPredicate#getValuesForArgPosition
                          -> KbPredicate#getValuesForArgPositionWithMatchArg
						  
    KbObject#formulaArity -> Assertion#getArity
                          -> KbTerm#getArity
                          -> Sentence#getArity

A few methods have also had their arg signatures modified to reflect these changes. In particular,
methods which had required a predicate for their first arg were moved to `KbPredicate` and their
arg signatures were updated accordingly. For example:

       KbObject    #addFact(Context ctx, KbPredicate pred, int thisArgPos, Object... otherArgs)
    -> KbPredicate #addFact(Context ctx, Object... args)
    
       KbObject    #getFact(Context ctx, KbPredicate pred, int thisArgPos, Object... otherArgs)
    -> KbPredicate #getFact(Context ctx, Object... args)
   	
       KbObject    #getFacts(KbPredicate pred, int thisArgPos, Context ctx)	 
    -> KbPredicate #getFacts(Object arg, int argPosition, Context ctx)
   	
       KbObject    #getSentence(KbPredicate pred, int thisArgPos, Object... otherArgs)	 
    -> KbPredicate #getSentence(Object... args)
    
       KbObject    #getValues(KbPredicate pred, int thisArgPos, int valuePosition, Context ctx)
    -> KbPredicate #getValuesForArgPosition(Object arg, int argPosition, int valuePosition, Context ctx)

This update also includes assorted refactorings which shouldn't impact existing application code: a 
few service provider interfaces have been renamed, some unnecessary generics have been removed from 
some SPIs, `com.cyc.query.graphs` is now `com.cyc.query.graph`, etc.

#### Query API

`com.cyc.query.QuerySpecification` no longer has any mutating methods on it; those are on the new 
`com.cyc.query.QuerySpecification.MutableQuerySpecification`, which is extended by 
`com.cyc.query.Query`. QuerySpecification is useful for cases where something wants to make the 
details of a Query available to external processes (for reporting, etc.) in a read-only fashion,
whereas MutableQuerySpecification can be used to create or modify queries & KBQs, and generally has
a pretty bean-like interface.

The map-like methods on `com.cyc.query.parameters.InferenceParameters` (`#keySet`, `#get`, 
`#putAll`, etc.) have been moved to `InferenceParameterGetter` and `InferenceParameterSetter`, as 
appropriate. `QuerySpecification#getInferenceParameters()` returns an InferenceParameterGetter, 
whereas MutableQuerySpecification & Query return a full, mutable, InferenceParameters instance.


1.0.0-rc6 - 2017-07-19
----------------------

Adds assorted new functionality. 1.0.0-rc6 is _not backwards-compatible_ with earlier API releases.

#### Query API

Adds support for ProofViews. To generate a ProofView:

1. Call `QueryFactory#getProofViewSpecification()` to retrieve a `ProofViewSpecification`.
2. Configure the ProofViewSpecification via its setters.
3. Then, call `QueryFactory#getProofView(QueryAnswer, ProofViewSpecification)`.

Also:

* Query now extends QuerySpecification, which solely represents the query itself; processing 
  information is still represented by Query.
* `Query#getAnswers()` now returns `QueryAnswers` instead of a raw `List<QueryAnswer>`. QueryAnswers
  extends `List<QueryAnswer>`, but adds several convenience methods to retrieve all bindings for a
  particular variable, get all bindings as a list for single-variable queries, get a single binding
  from queries which should return exactly one binding, and to print answers in a formatted table.
* Query rules can be retrieved via `QuerySpecification#getRules()`.
* Query indexicals cans be retrieved via `QuerySpecification#getUnresolvedIndexicals()`.

#### KB API

Improves support for working with quoted terms:

* KbObject#isQuoted()
* KbObject#quote()
* KbObject#unquote()
* KbObject#toQuoted()
* KbObject#toUnquoted()

Improves support for working with indexicals and variables:

* KbObject#isIndexical()
* KbObject#possiblyResolveIndexical(Map)
* KbObject#resolveIndexical()
* Sentence#getIndexicals(boolean)
* Sentence#getVariables(boolean)

Improves support for performing substitutions:

* KbTerm#replaceTerms(Map)
* Sentence#replaceTerms(Map)

#### Other changes

* Assorted minor changes to API methods.


1.0.0-rc5 - 2015-12-18
----------------------

The fifth release candidate of Cycorp's Java API suite, and the first in which the Core API 
specification and the Core Client implementation are packaged as wholly separate artifacts.

1.0.0-rc5 is _not backwards-compatible_ with earlier API releases. Note that _ResearchCyc 4.0q_ and
_EnterpriseCyc 1.7-preview_ require server code patching for compatibility with the 1.0.0-rc5 
release. See the Cyc Core Client for details.

The overarching goal of this revision is to be the last release before 1.0.0 with any compatibility-
breaking changes. Note, however, that this is not a _guarantee:_ additional disruptive changes may 
be made before 1.0.0 if deemed necessary.

#### API & implementation division

There is now a clean division between the Core APIs and their reference implementation; they have
been split into two artifacts:

* The implementation-independent Core API specification: `core-api-spec`
* The Core Client reference implementation: `cyc-core-client-impl`

Details:

* The `core-api-suite` project has been split into two sibling projects: `core-api-spec`and 
  `core-client`.
* The relevant artifacts generated by these projects (the artifacts that projects are typically 
  expected to specify as dependencies) are `cyc-core-api` and `cyc-core-client-impl`, respectively.
* Introduces implementation-independent factory classes for Core API objects. Code written against 
  the `cyc-core-api` should typically not need to refer to any implementation-specific classes or 
  methods (although an implementation library will need to be present at run-time.)

In short, you'll typically need to include two dependencies to use the Core APIs. For example, in 
Maven:

    <dependency>
      <!-- Core API specification -->
      <groupId>com.cyc</groupId>
      <artifactId>cyc-core-api</artifactId>
      <version>1.0.0-rc5</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <!-- Reference implementation for the Core API -->
      <groupId>com.cyc</groupId>
      <artifactId>cyc-core-client-impl</artifactId>
      <version>1.0.0-rc5</version>
      <scope>runtime</scope>
    </dependency>

**Caveat:** Some functionality, such as proofview generation, is not yet represented in the Core API
spec, so there are still some cases in which the user might need to write code against
cyc-core-client-impl. This functionality will be added to the Core API Spec in an upcoming revision.

#### API consistency

* All `cyc-core-api` class names and method signatures conform to the
  [Google Java Style](https://google.github.io/styleguide/javaguide.html) coding standards.

#### Other changes

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


1.0.0-rc4 - 2015-10-29
----------------------

The fourth release candidate of Cycorp's Java API suite. 1.0.0-rc4 is _not backwards-compatible_
with earlier API releases. Note that _ResearchCyc 4.0q_ and _EnterpriseCyc 1.7-preview_ require
[server code patching](server-patching.md) for compatibility with the 1.0.0-rc4 release.

#### Query API

* Instead of creating Query objects directly, call QueryFactory.getQuery(...)
* Query API no longer uses BaseClient CycObjects.

#### Natural language

* Adds com.cyc.nl package to Core API Specification to support upcoming release of NL API.
* Split `com.cyc.baseclient.nl.Paraphraser` class into `com.cyc.nl.Paraphraser` interface (in Core
  API Spec) and `com.cyc.baseclient.nl.ParaphraserFactory` class (in Base Client).

#### Session API

* Adds API support for closing CycSessions and SessionManagers. See 
  [Session API Connection Management](http://dev.cyc.com/api/core/session/connection-management/) 
  for details.
* Significant improvements to session resource management, especially CycAccess management.
* Bug fixes for threading issues.

#### Other changes

* Critical bug fixes related to obfuscated functions in the planned upcoming release of
  OpenCyc 5.0-preview.
* Assorted bug fixes.


1.0.0-rc3 - 2015-08-14
----------------------

The third release candidate of Cycorp's Java API suite. 1.0.0-rc3 is _not backwards-compatible_
with earlier API releases. Note that _ResearchCyc 4.0q_ and _EnterpriseCyc 1.7-preview_ require
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

The second release candidate of Cycorp's Java API suite for interacting with the Cyc inference
engine and knowledge base. Note that 1.0.0-rc2 is _not backwards-compatible_ with earlier API
releases.

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

First release candidate of Cycorp's Java API suite for interacting with the Cyc inference engine
and knowledge base. Note that 1.0.0-rc1 is _not backwards-compatible_ with earlier API releases.

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
