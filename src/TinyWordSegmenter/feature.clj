(ns TinyWordSegmenter.feature)

(import 'java.lang.Character$UnicodeBlock)

(defstruct feature :type :str)

(defn is-numeric? [c]
  (Character/isDigit c))

(defn is-hiragana? [c]
  (= Character$UnicodeBlock/HIRAGANA
     (Character$UnicodeBlock/of c)))

(defn is-katakana? [c]
  (= Character$UnicodeBlock/KATAKANA
     (Character$UnicodeBlock/of c)))

(defn is-symbol? [c]
  (contains? #{\。 \、 \！ \？ \. \, \! \?} c))

(defn get-char-type [c]
  (cond (is-numeric? c) 0
	(is-hiragana? c) 1
	(is-katakana? c) 2
	(is-symbol? c) 3
	:else 4))

(defn get-type-bigram-feature [substr]
  (let [v (vec (map get-char-type substr))]
    (struct feature (+ 7 (first v) (* 5 (second v))) "")))