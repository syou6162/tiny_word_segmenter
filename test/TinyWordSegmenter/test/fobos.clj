(ns TinyWordSegmenter.test.fobos
  (:use [TinyWordSegmenter.fobos])
  (:use [clojure.test]))

(deftest test-dotproduct
  (is (= 0.0
       (dotproduct {0 0.1,
		    1 0.2,
		    2 0.5,
		    3 -0.1}
		   [[1 1] [3 2]]))))
