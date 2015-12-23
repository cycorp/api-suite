;; Facilitates manual loading of the SubL API patches.
;; This file is *not* intended to be applied by the SubLResourceLoader.
;; 
;; For instructions on how to apply these patches to a Cyc server,
;; see the CycCoreAPI README or http://dev.cyc.com/api/server-patching/
;; 
;; author: nwinant
;; Added to the Base Client on 2015-07-08 per CAPI-275.


(punless (fboundp 'categorize-term-wrt-api)
  (load "init/api-patches/categorize-term-wrt-api.lisp"))

(format t "Loaded compatibility patches for Cyc Core API Suite v1.0.0-rc3.")


