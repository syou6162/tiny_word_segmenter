(ns TinyWordSegmenter.test.fobos
  (:use [TinyWordSegmenter.fobos])
  (:use [clojure.test]))

(deftest test-clip-by-zero
  (is (= (clip-by-zero 1.0 0.5) 0.5))
  (is (= (clip-by-zero 1.0 1.5) 0.0))
  (is (= (clip-by-zero -1.0 0.5) -0.5))
  (is (= (clip-by-zero -1.0 1.5) 0.0)))

(deftest test-dotproduct
  (is (= 0.0
       (dotproduct {0 0.1,
		    1 0.2,
		    2 0.5,
		    3 -0.1}
		   [[1 1] [3 2]]))))

(deftest test-get-eta
  (is (= (/ 1.0 (+ 1.0 (/ 3 100)))
	 (get-eta 3 100))))

(deftest test-l1-regularize
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	iter 100
	example-size 1000
	lambda 1.0]
    (is (empty
	 (l1-regularize weight iter example-size lambda)))))

(deftest test-add-example
  (let [init [[[[1 1] [2 1]] 1]
	      [[[1 2] [3 2]] -1]]]
    (is (= [[[[1 1] [2 1]] 1]
	    [[[1 2] [3 2]] -1]
	    [[[1 1] [4 1]] 1]]
	     (add-example init [[1 1] [4 1]] 1)))))
