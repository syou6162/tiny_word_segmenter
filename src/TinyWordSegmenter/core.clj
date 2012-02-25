(ns TinyWordSegmenter.core
  (:use [clojure.set :only (difference)])
  (:use [clojure.contrib.command-line :only (with-command-line)])
  (:use [TinyWordSegmenter.feature])
  (:use TinyWordSegmenter.fobos)
  (:use TinyWordSegmenter.util)
  (:use TinyWordSegmenter.svm)
  (:use TinyWordSegmenter.decoder)
  (:gen-class))

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

(defn split-train-and-test [examples]
  ;; 10%はテストデータに
  (let [N-of-test (/ (count examples) 10)]
    (reverse (split-at N-of-test examples))))

(defn training-mode [opts]
  (let [[train-examples test-examples] (split-train-and-test (get-examples (opts :file)))
	gold (vec (map first test-examples))
	max-iter (opts :max-iter)
	eta (opts :eta)
	lambda (opts :lambda)
	init-weight (update-weight train-examples {} 0 eta lambda)]
    (loop [iter 1
	   weight init-weight]
      (if (= iter max-iter)
	(spit (opts :model-file) weight)
	(do
	  (println iter ":"
		   (count weight) ":"
		   (get-f-value gold (map #(if (> (dotproduct weight (second %)) 0.0) 1 -1) test-examples)))
	  (recur (inc iter)
		 (update-weight train-examples weight iter eta lambda)))))))

(defn decoding-mode [opts]
  (letfn [(decode-demo [weight]
		       (print "> ")
		       (flush)
		       (let [s (read-line)]
			 (println (decode weight s))
			 (recur weight)))]
    (let [weight (read-string (slurp (opts :model-file)))]
      (decode-demo weight))))

(defn -main [& args]
  (with-command-line args "comment"
    [[file "File name of training and test"]
     [mode "Training mode or decoding mode"]
     [eta "Update step"]
     [lambda "Regularization parameter"]
     [max-iter "Number of maximum iterations"]
     [model-file "File name of trained model file"]
     rest]
    (let [opts {:file file
		:model mode
		:eta (try (Double/parseDouble eta)
			  (catch Exception e 1.0))
		:lambda (try (Double/parseDouble lambda)
			     (catch Exception e 1.0))
		:max-iter (try (Integer/parseInt max-iter)
			       (catch Exception e 100))
		:model-file model-file}]
      (cond (= mode "train") (training-mode opts)
	    (= mode "decode") (decoding-mode opts)
	    :else (do
		    (println "No such mode:" mode)
		    (println "Select --mode (train|decode)"))))))