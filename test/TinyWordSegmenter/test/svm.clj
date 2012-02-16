(ns TinyWordSegmenter.test.svm
  (:use [TinyWordSegmenter.svm])
  (:use [clojure.test]))

(deftest test-margin
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	fv [[1 1] [2 1]]]
    (margin weight fv 1)))

(deftest test-update
  (let [examples [[[[1 1] [2 1]] 1]
		  [[[1 2] [2 0]] -1]]
	init-weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	max-iter 10
	eta 0.1
	lambda 0.1]
    (update examples init-weight max-iter eta lambda)))

(deftest test-classify
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}]
    (classify weight [[1 1] [2 1]] 1)))