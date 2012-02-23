(ns TinyWordSegmenter.core
  (:use [clojure.contrib.string :only (split)]))

(defn -main [& args]
  (vec (map #(vec (split #"\n" %))
	    (split #"\nEOS\n" (slurp "KyotoUniv.txt")))))
