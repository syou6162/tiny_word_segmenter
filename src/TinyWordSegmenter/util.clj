(ns TinyWordSegmenter.util
  (:use [clojure.contrib.string :only (split)]))

(defn parse-line [line]
  (let [[y fv] (-> (re-seq #"([-+]?1)\s(.*)" line)
		   (first)
		   (rest))]
    [(if (= y "+1") 1 -1)
     (->> fv
	  (split #"\s")
	  (map #(let [[xi cnt] (split #":" %)]
		  [xi (Double/parseDouble cnt)]))
	  (vec))]))

