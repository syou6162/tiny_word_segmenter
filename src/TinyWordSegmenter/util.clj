(ns TinyWordSegmenter.util
  (:use [clojure.contrib.string :only (split)]))

(defn parse-line [line]
  (let [[y fv] (-> (re-seq #"(-?1)\s(.*)" line)
		   (first)
		   (rest))]
    [(Integer/parseInt y) (->> fv
			       (split #"\s")
			       (map #(let [[xi cnt] (split #":" %)]
				       [(Integer/parseInt xi) (Double/parseDouble cnt)]))
			       (vec))]))
