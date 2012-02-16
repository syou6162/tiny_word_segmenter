(ns TinyWordSegmenter.test.util
  (:use [TinyWordSegmenter.util])
  (:use [clojure.test]))

(deftest test-parse-line
  (is (= (parse-line "1 1:2 2:3")
	 [1 [[1 2] [2 3]]])))
