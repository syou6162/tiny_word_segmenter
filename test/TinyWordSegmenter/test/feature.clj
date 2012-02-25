(ns TinyWordSegmenter.test.feature
  (:use [TinyWordSegmenter.feature])
  (:use [clojure.test]))

(deftest test-get-type-bigram-feature
  (is (= (get-type-bigram-feature "AB" 1)
	 [(struct feature 31 "") 1.0]))
  (is (= (get-type-bigram-feature "あい" 1)
	 [(struct feature 13 "") 1.0]))
  (is (= (get-type-bigram-feature "日本" 1)
	 [(struct feature 31 "") 1.0])))

(deftest test-get-unigram-feature
  (is (= (get-unigram-feature "ABCDEFG" 4)
	 [[{:type 0, :str "C"} 1.0]
	  [{:type 1, :str "D"} 1.0]
	  [{:type 2, :str "E"} 1.0]
	  [{:type 3, :str "F"} 1.0]]))
  (is (= (get-unigram-feature "ABCDEFG" 1)
	 [[{:type 0, :str ""} 1.0]
	  [{:type 1, :str "A"} 1.0]
	  [{:type 2, :str "B"} 1.0]
	  [{:type 3, :str "C"} 1.0]]))
  (is (= (get-unigram-feature "ABCDEFG" 6)
	 [[{:type 0, :str "E"} 1.0]
	  [{:type 1, :str "F"} 1.0]
	  [{:type 2, :str "G"} 1.0]
	  [{:type 3, :str ""} 1.0]]))
  (is (= (get-unigram-feature "AB" 1)
	 [[{:type 0, :str ""} 1.0]
	  [{:type 1, :str "A"} 1.0]
	  [{:type 2, :str "B"} 1.0]
	  [{:type 3, :str ""} 1.0]]))
  (is (= (get-unigram-feature "A" 1)
	 [[{:type 0, :str ""} 1.0]
	  [{:type 1, :str "A"} 1.0]
	  [{:type 2, :str ""} 1.0]
	  [{:type 3, :str ""} 1.0]]))
  (is (= (get-unigram-feature "" 0)
	 [[{:type 0, :str ""} 1.0]
	  [{:type 1, :str ""} 1.0]
	  [{:type 2, :str ""} 1.0]
	  [{:type 3, :str ""} 1.0]])))

(deftest test-get-bigram-feature
  (is (= (get-bigram-feature "ABCDEFG" 4)
	 [{:type 5, :str "DE"} 1.0]))
  (is (= (get-bigram-feature "A" 1)
	 [{:type 5, :str "A"} 1.0]))
  (is (= (get-bigram-feature "" 1)
	 [{:type 5, :str ""} 1.0])))

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
