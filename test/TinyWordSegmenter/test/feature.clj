(ns TinyWordSegmenter.test.feature
  (:use [TinyWordSegmenter.feature])
  (:use [clojure.test]))

(deftest test-get-type-bigram-feature
  (is (= (get-type-bigram-feature "AB")
	 (struct feature 31 "")))
  (is (= (get-type-bigram-feature "あい")
	 (struct feature 13 "")))
  (is (= (get-type-bigram-feature "日本")
	 (struct feature 31 ""))))

(deftest test-get-fv
  (is (= (get-fv "日本" 0)
	 [[(struct feature 31 "") 1.0]]))
  (is (= (map (fn [center] (get-fv "日本は" center))
	      (range 2)) 
	 (list [[(struct feature 31 "") 1.0]]
	       [[(struct feature 16 "") 1.0]]))))
