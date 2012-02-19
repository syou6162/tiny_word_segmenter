(ns TinyWordSegmenter.test.util
  (:use [TinyWordSegmenter.util])
  (:use [clojure.test]))

(deftest test-parse-line
  (is (= (parse-line "1 1:2 2:3")
	 [1 [[1 2.0] [2 3.0]]]))
  (is (= (parse-line "-1 1:2.05 2:0.1")
	 [-1 [[1 2.05] [2 0.1]]])))
