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