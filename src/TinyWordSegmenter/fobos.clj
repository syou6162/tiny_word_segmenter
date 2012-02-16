(ns TinyWordSegmenter.fobos)

(defn clip-by-zero [a b]
  (if (> a 0.0)
    (if (> a b) (- a b) 0.0)
    (if (< a (- b)) (+ a b) 0.0)))

(defn dotproduct "内積"
  [weight fv]
  (reduce (fn [sum [k v]]
	    (+ sum
	       (* v (get-in weight [k] 0.0))))
	  0.0 fv))

(defn get-eta [iter example-size]
  "各iterationで重みを減衰させていく"
  (/ 1.0 (+ 1.0 (/ iter example-size))))

(defn l1-regularize 
  "L1正則化をかけて、sparseにした重みベクトルを返す"
  [weight iter example-size lambda]
  (let [lambda-hat (* (get-eta iter example-size) lambda)]
    (reduce (fn [result [k v]]
	      (let [tmp-result (assoc result k (clip-by-zero v lambda-hat))]
		(if (< (Math/abs v) lambda-hat)
		  (dissoc tmp-result k)
		  tmp-result)))
	    weight weight)))

(defn add-example [examples fv y]
  (conj examples [fv y]))

(defn muladd [weight fv y scale]
  (reduce (fn [result [k xi]]
	    (assoc result k (+ (get-in result [k] 0.0)
			       (* y xi scale))))
	  weight fv))