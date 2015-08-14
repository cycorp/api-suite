;; Supports com.cyc.base.kbtool.InspectorTool#categorizeTermWRTApi, which improves performance of 
;; KBObjectFactory.get(CycObject, Class) &  KBObjectImpl.convertToKBObject(CycObject).
;;
;; author: vijay
;; Added to the Base Client on 2015-05-22 per CAPI-597.


(defparameter-private *java-api-collection-hierarchy* 
    '(#$Thing
      (#$Collection
       (#$FirstOrderCollection #$SecondOrderCollection))
      (#$Individual
       (#$Microtheory
	(#$Relation #$Function-Denotational
	 (#$Predicate #$BinaryPredicate) #$LogicalConnective)
	#$Quantifier))))

(define-external categorize-term-wrt-api (term &optional (init-col #$Thing))
  "Find and return the most specific collection that <code>term</code> is an instance of in 
   the tree: (#$Thing
			  (#$Collection
			   (#$FirstOrderCollection #$SecondOrderCollection))
			  (#$Individual
			   (#$Microtheory
			    (#$Relation #$Function-Denotational
			     (#$Predicate #$BinaryPredicate))
			    #$Quantifier)))."
  (ignore init-col)
  (clet ((positions 
	  (tree-positions-dfs term *java-api-collection-hierarchy* #'quick-quiet-has-type?)))
    (pwhen positions
      (ret (get-nested-nth *java-api-collection-hierarchy* (car (last positions)))))))


