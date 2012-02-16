(ns TinyWordSegmenter.svm
  (:use [TinyWordSegmenter.fobos]))

(defn margin [weight fv y]
  (* (dotproduct weight fv) y))

(defn update [examples init-weight max-iter eta lambda]
  (let [example-size (count examples)]
    (loop [iter 0
	   weight init-weight]
      (if (= iter max-iter)
	weight
	(l1-regularize
	 (reduce (fn [result [fv y]]
		   (if (< (margin weight fv y) 1.0)
		     (muladd result fv y eta)
		     result))
		 weight examples)
	 iter example-size lambda)))))

(defn classify [weight fv y]
  (> (margin weight fv y) 0.0))
