(ns TinyWordSegmenter.decoder
  (:use fobos_clj.fobos)
  (:use fobos_clj.svm)
  (:use fobos_clj.logistic)
  (:use [TinyWordSegmenter.feature]))

(defn decode [model line]
  (split-by-pos line
		(conj (->> (range (count line))
			   (rest)
			   (map
			    #(if (= (classify model (get-fv line %)) 1)
			       % false))
			   (filter identity)
			   (cons 0)
			   (vec))
		      (count line))))
