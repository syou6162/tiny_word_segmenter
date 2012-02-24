(ns TinyWordSegmenter.decode-demo
  (:use TinyWordSegmenter.feature)
  (:use TinyWordSegmenter.fobos)
  (:use TinyWordSegmenter.util)
  (:use TinyWordSegmenter.svm)
  (:use TinyWordSegmenter.decoder))

(defn decode-demo [weight]
  (print "> ")
  (flush)
  (let [s (read-line)]
    (println (decode weight s))
    (recur weight)))

(defn -main [& args]
  (let [weight (read-string (slurp "./weight.model"))]
    (decode-demo weight)))
