(ns TinyWordSegmenter.decoder
  (:use TinyWordSegmenter.fobos)
  (:use [TinyWordSegmenter.feature]))

(defn decode [weight line]
  (->> (count line)
       (range)
       (rest)
       (map
	#(if (> (dotproduct weight (get-fv line %)) 0.0)
	   % false))
       (filter identity)
       (cons 0)
       (split-by-pos line)))
