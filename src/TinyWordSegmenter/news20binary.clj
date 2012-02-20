(ns TinyWordSegmenter.news20binary
  (:use TinyWordSegmenter.util)
  (:use TinyWordSegmenter.fobos)
  (:use TinyWordSegmenter.svm)
  (:use [clojure.contrib.duck-streams :only (reader read-lines)]))

(defn -main [& args]
  (let [train-examples (vec (remove nil? (for [line (read-lines "train.txt")]
					   (try (parse-line line)
						(catch Exception e nil)))))
	test-examples (vec (remove nil? (for [line (read-lines "test.txt")]
					  (try (parse-line line)
					       (catch Exception e nil)))))
	gold (vec (map first test-examples))
	max-iter 1000
	eta 1.0
	lambda 1.0
	init-weight (update-weight train-examples {} 0 eta lambda)]
    (loop [iter 1
	   weight init-weight]
      (if (= iter max-iter)
	weight
	(do
	  (println iter ":"
		   (count train-examples) ":"
		   (count weight) ":"
		   (get-f-value gold (map #(if (> (dotproduct weight (second %)) 0.0) 1 -1) test-examples)))
	  (recur (inc iter)
		 (update-weight train-examples weight iter eta lambda))))))
  nil)