(ns TinyWordSegmenter.decoder
  (:use TinyWordSegmenter.fobos)
  (:use [TinyWordSegmenter.feature]))

(defn decode [weight line]
  (split-by-pos line
		(conj (->> (range (count line))
			   (rest)
			   (map
			    #(if (>= (dotproduct weight (get-fv line %)) 0.0)
			       % false))
			   (filter identity)
			   (cons 0)
			   (vec))
		      (count line))))
