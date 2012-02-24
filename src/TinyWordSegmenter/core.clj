(ns TinyWordSegmenter.core
  (:use [TinyWordSegmenter.feature])
  (:use [clojure.contrib.string :only (split)])
  (:use [clojure.set :only (difference)]))

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
  (->>(slurp "KyotoUniv.txt")
      (get-splitted-words-from-lines)
      (map
       (fn [words]
	 (let [line (apply str words)
	       cuts (butlast (rest (get-cut-pos words)))
	       not-cuts (vec (difference (set (range (inc (count line))))
					 (set (get-cut-pos words))))]
	   (concat
	    (map #(vector (get-fv line %) 1) cuts)
	    (map #(vector (get-fv line %) -1) not-cuts)))))
      (apply concat)
      (vec)))
