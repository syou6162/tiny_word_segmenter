(ns TinyWordSegmenter.feature
  (:use [clojure.contrib.string :only (split)]))

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

(defn get-unigram-feature [str-arg center]
  (->> (list
	(if (neg? (- center 2))
	  (struct feature 0 "")
	  (struct feature 0 (subs str-arg (- center 2) (- center 1))))
	(if (neg? (- center 1))
	  (struct feature 1 "")
	  (struct feature 1 (subs str-arg (- center 1) center)))
	(if (>= center (count str-arg))
	  (struct feature 2 "")
	  (struct feature 2 (subs str-arg center (+ center 1))))
	(if (>= (+ center 1) (count str-arg))
	  (struct feature 3 "")
	  (struct feature 3 (subs str-arg (+ center 1) (+ center 2)))))
       (map #(vector % 1.0))
       (vec)))

(defn get-bigram-feature [str-arg center]
  (cond
   (empty? str-arg) (vector (struct feature 5 "") 1.0) ;; 空文字
   (<= (count str-arg) center) (vector (struct feature 5 (subs str-arg (- center 1) center)) 1.0) ;; 1文字
   :else (vector (struct feature 5 (subs str-arg (- center 1) (+ center 1))) 1.0))) ;; 2文字以上

(defn get-fv
  "centerを中心としたfeature vectorを生成する"
  [str-arg center]
  (let [result []]
    (concat
     result
     [[(get-type-bigram-feature
	(subs str-arg (dec center) (inc center))) 1.0]]
     (get-unigram-feature str-arg center)
     [(get-bigram-feature str-arg center)])))

(defn get-cut-pos [words]
  (reductions
   (fn [cum str] (+ cum (count str)))
   0 words))

(defn split-by-pos [str-arg pos]
  (map (fn [[start end]] (subs str-arg start end))
       (partition 2 1 pos)))

(defn get-splitted-words-from-lines [lines]
  (vec (map #(vec (split #"\n" %))
	    (split #"\nEOS\n" lines))))
