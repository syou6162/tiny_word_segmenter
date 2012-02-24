(ns TinyWordSegmenter.core
  (:use [clojure.set :only (difference)])
  (:use [TinyWordSegmenter.feature])
  (:use TinyWordSegmenter.fobos)
  (:use TinyWordSegmenter.util)
  (:use TinyWordSegmenter.svm)
  (:use TinyWordSegmenter.decoder))

(defn get-examples [filename]
  (->>(slurp filename)
      (get-splitted-words-from-lines)
      (map
       (fn [words]
	 (let [line (apply str words)
	       cuts (butlast (rest (get-cut-pos words)))
	       not-cuts (vec (difference (set (range (inc (count line))))
					 (set (get-cut-pos words))))]
	   (concat
	    (map #(vector 1 (get-fv line %)) cuts)
	    (map #(vector -1 (get-fv line %)) not-cuts)))))
      (apply concat)
      (shuffle)
      (vec)))

(defn -main [& args]
  (let [[test-examples train-examples] (split-at 1000 (get-examples "KyotoUniv.txt"))
	gold (vec (map first test-examples))
	max-iter 1000
	eta 1.0
	lambda 1.0
	init-weight (update-weight train-examples {} 0 eta lambda)]
    (loop [iter 1
	   weight init-weight]
      (if (= iter max-iter)
	(spit "./weight.model" weight)
	(do
	  (println iter ":"
		   (count weight) ":"
		   (get-f-value gold (map #(if (> (dotproduct weight (second %)) 0.0) 1 -1) test-examples))
		   (decode weight "日本語です。Tokyo")
		   (decode weight "無茶ぶりを他人に割り振る。うーむこれが中間管理職か。いやな仕事だ。ごめんよダニエル")
		   (decode weight "研究室にあったコイツを試してみた。確かに眼は覚めたけど、それ以上に味が不味いよこれ…"))
	  (recur (inc iter)
		 (update-weight train-examples weight iter eta lambda)))))))