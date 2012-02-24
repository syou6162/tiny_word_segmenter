(ns TinyWordSegmenter.core
  (:use [clojure.contrib.string :only (split)]))

(defn get-cut-pos [words]
  (reductions
   (fn [cum str] (+ cum (count str)))
   0 words))


(defn -main [& args]
  (vec (map #(vec (split #"\n" %))
	    (split #"\nEOS\n" (slurp "KyotoUniv.txt")))))
