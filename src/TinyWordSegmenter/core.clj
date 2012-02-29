(ns TinyWordSegmenter.core
  (:use [clojure.set :only (difference)])
  (:use [clojure.contrib.command-line :only (with-command-line)])
  (:use [clojure.contrib.core :only (new-by-name)])
  (:use fobos_clj.util)
  (:use fobos_clj.fobos)
  (:use fobos_clj.svm)
  (:use fobos_clj.logistic)
  (:use TinyWordSegmenter.feature)
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
	model-name (opts :model)
	init-model (update-weight
		    (new-by-name model-name train-examples {} eta lambda)
		    0)]
    (loop [iter 1
	   model init-model]
      (if (= iter max-iter)
	(spit (opts :model-file) {:model-name (.getName (class model))
				  :weight (:weight model)})
	(do
	  (println (str iter ", "
			(count (:weight model)) ", "
			(get-f-value gold (map #(classify model (second %)) test-examples))))
	  (recur (inc iter)
		 (update-weight model iter)))))))

(defn decoding-mode [opts]
  (letfn [(decode-demo [model]
		       (print "> ")
		       (flush)
		       (let [s (read-line)]
			 (println (decode model s))
			 (recur model)))]
    (let [model-info (read-string (slurp (opts :model-file)))
	  model (new-by-name (:model-name model-info) nil (:weight model-info) nil nil)]
      (decode-demo model))))

(defn -main [& args]
  (with-command-line args "comment"
    [[file "File name of training and test"]
     [mode "Training mode or decoding mode"]
     [eta "Update step"]
     [lambda "Regularization parameter"]
     [max-iter "Number of maximum iterations"]
     [model-file "File name of trained model file"]
     [model "Model to use. Currently, we support fobos_clj.svm.SVM and fobos_clj.logistic.Logistic"]
     rest]
    (let [opts {:file file
		:eta (try (Double/parseDouble eta)
			  (catch Exception e 1.0))
		:lambda (try (Double/parseDouble lambda)
			     (catch Exception e 1.0))
		:max-iter (try (Integer/parseInt max-iter)
			       (catch Exception e 100))
		:model-file model-file
		:model model}]
      (cond (= mode "train") (training-mode opts)
	    (= mode "decode") (decoding-mode opts)
	    :else (do
		    (println "No such mode:" mode)
		    (println "Select --mode (train|decode)"))))))