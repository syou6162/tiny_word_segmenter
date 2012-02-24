(ns TinyWordSegmenter.test.feature
  (:use [TinyWordSegmenter.feature])
  (:use [clojure.test]))

(deftest test-get-type-bigram-feature
  (is (= (get-type-bigram-feature "AB")
	 
	 (struct feature 31 "")))
  (is (= (get-type-bigram-feature "あい")
	 (struct feature 13 "")))
  (is (= (get-type-bigram-feature "日本")
	 (struct feature 31 ""))))

(deftest test-get-fv
  (is (= (get-fv "日本" 1)
	 [[(struct feature 31 "") 1.0]]))
  (is (= (map (fn [center] (get-fv "日本は" center))
	      (range 1 3))
	 (list [[(struct feature 31 "") 1.0]]
	       [[(struct feature 16 "") 1.0]]))))

(deftest test-get-cut-pos
  (is (= (get-cut-pos ["出口" "さん" "の" "失跡" "を" "警察" "に"
		       "届けた" "兄" "の" "執念" "も" "、" "警察"
		       "を" "動かした" "。"])
	 '(0 2 4 5 7 8 10 11 14 15 16 18 19 20 22 23 27 28))))

(deftest test-split-by-pos
  (is (= (split-by-pos
	  "出口さんの失跡を警察に届けた兄の執念も、警察を動かした。"
	  '(0 2 4 5 7 8 10 11 14 15 16 18 19 20 22 23 27 28)))))

(deftest test-get-splitted-words-from-lines
  (is (= (get-splitted-words-from-lines "村山
富市
首相
は
年頭
EOS
あたり
首相
官邸
EOS
内閣
記者
会")
	 [["村山"
	   "富市"
	   "首相"
	   "は"
	   "年頭"]
	  ["あたり"
	   "首相"
	   "官邸"]
	  ["内閣"
	   "記者"
	   "会"]])))
