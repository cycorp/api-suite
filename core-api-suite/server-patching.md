Server Code Patching
====================

As of version 1.0.0-rc3, the Core API Suite requires SubL code patches which are not present in
_ResearchCyc 4.0q_ or _EnterpriseCyc 1.7-preview_. These patches can be applied manually, or they
can be automatically applied by the API bundle itself.


Automatic patching
------------------

As of version 1.0.0-rc3, the Core API Suite can detect and apply missing SubL code patches to a Cyc
server. This is not intended as a general update mechanism, but to enable API functionality by
applying a small set of bundled, focused patches as necessary. This feature is only designed to
apply specific patches which have been bundled with the Core API Suite, and is not designed to
download arbitrary patches from a network.

For example: When returning KBObjects, `com.cyc.kb.client.KBObjectImpl#convertToKBObject(CycObject)`
does some work to find the most specific subtype of KBObject for a particular term. Early
implementations performed this as a client-side decision tree: this resulted in multiple calls to
Cyc, which meant that things could get fairly slow when returning many KBObjects; this in turn
slowed down the processing of query results. In version 1.0.0-rc3, this was reimplemented to push
the tightening code to Cyc, but previously-supported Cyc releases (ResearchCyc 4.0q and
EnterpriseCyc 1.7-preview) do not have the required SubL function. Thus, a patch file is bundled
with the Core API Suite which can be automatically applied if the required SubL function is missing,
and compatibility is maintained.

This behavior is disabled by default, but may be enabled by the `cyc.session.server.patchingAllowed`
property. For example, if running the Core API test suite from the command line against a Cyc server
at localhost:3600:

    mvn verify -Dcyc.session.server.patchingAllowed=true -Dcyc.session.server=localhost:3600


Manual patching
---------------
First, you'll need a copy of the subl `api-patches` directory, which can be found in the sources at
`CycCoreAPI/BaseClient/src/main/resources/subl/api-patches`. If you don't have them already, you can 
[download the 1.0.0-rc3 sources here](https://github.com/cycorp/CycCoreAPI/releases/tag/v1.0.0-rc3).

Then:

1. Copy the `api-patches` directory into your Cyc server's `init` directory. In _ResearchCyc 4.0q_ 
   and _EnterpriseCyc 1.7-preview_, this lives at `server/cyc/run/init`.
2. You should now have a `server/cyc/run/init/api-patches` directory containing multiple `*.lisp`
   files, including `init-api-patches.lisp`
3. Edit `init/jrtl-release-init.lisp`. At the end of the file, add the following: 


    ;; Load compatibility patches for Cyc Core APIs
    ;; For more information, see http://dev.cyc.com/api/server-patching/
    (load "init/api-patches/init-api-patches.lisp")

The next time you start up your server, it should be patched.
