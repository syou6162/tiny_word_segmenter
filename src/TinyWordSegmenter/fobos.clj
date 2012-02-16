(ns TinyWordSegmenter.fobos)

(defn clip-by-zero [a b]
  (if (> a 0.0)
    (if (> a b) (- a b) 0.0)
    (< (a (- b)) (+ a b) 0.0)))

(defn dotproduct "内積"
  [weight fv]
  (reduce (fn [sum [k v]]
	    (+ sum
	       (* v (get-in weight [k] 0.0))))
	  0.0 fv))
