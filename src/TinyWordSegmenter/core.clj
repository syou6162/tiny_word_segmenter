(ns TinyWordSegmenter.core
  (:use [TinyWordSegmenter.feature])
  (:use TinyWordSegmenter.fobos)
  (:use TinyWordSegmenter.util)
  (:use TinyWordSegmenter.svm)
  (:use TinyWordSegmenter.decoder)
  (:use [clojure.set :only (difference)]))

(defn get-examples [filename]
  (->>(slurp filename)
      (get-splitted-words-from-lines)
      (take 10000)
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
  (let [[train-examples test-examples] (split-at 300000 (get-examples "KyotoUniv.txt"))
	gold (vec (map first test-examples))
	max-iter 10
	eta 0.01
	lambda 0.1
	init-weight (update-weight train-examples {} 0 eta lambda)]
    (loop [iter 1
	   weight init-weight]
      (if (= iter max-iter)
	weight
	(do
	  (println iter ":"
		   (count weight) ":"
		   (get-f-value gold (map #(if (> (dotproduct weight (second %)) 0.0) 1 -1) test-examples))
		   (decode weight "日本語です。Tokyo")
		   (decode weight "無茶ぶりを他人に割り振る。うーむこれが中間管理職か。いやな仕事だ。ごめんよダニエル"))
	  (recur (inc iter)
		 (update-weight train-examples weight iter eta lambda)))))))
