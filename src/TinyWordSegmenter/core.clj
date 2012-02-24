(ns TinyWordSegmenter.core
  (:use [clojure.contrib.string :only (split)]))

(defn get-cut-pos [words]
  (reductions
   (fn [cum str] (+ cum (count str)))
   0 words))

(defn split-by-pos [str-arg pos]
  (map (fn [[start end]] (subs str-arg start end))
       (partition 2 1 pos)))

(defn get-splitted-words-from-lines [lines]
  (vec (map #(vec (split #"\n" %))
	    (split #"\nEOS\n" lines))))

(defn -main [& args]
  (get-splitted-words-from-lines (slurp "KyotoUniv.txt")))
