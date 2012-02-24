(ns TinyWordSegmenter.test.core
  (:use [TinyWordSegmenter.core])
  (:use [clojure.test]))

(deftest test-get-cut-pos
  (is (= (get-cut-pos ["出口" "さん" "の" "失跡" "を" "警察" "に"
		       "届けた" "兄" "の" "執念" "も" "、" "警察"
		       "を" "動かした" "。"])
	 '(0 2 4 5 7 8 10 11 14 15 16 18 19 20 22 23 27 28))))

(deftest test-split-by-pos
  (is (= (split-by-pos
	  "出口さんの失跡を警察に届けた兄の執念も、警察を動かした。"
	  '(0 2 4 5 7 8 10 11 14 15 16 18 19 20 22 23 27 28)))))