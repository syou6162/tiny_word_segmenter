(defproject TinyWordSegmenter "0.0.2"
  :description "Tiny word segmenter written in clojure"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
                 [org.apache.commons/commons-math "2.2"]]
  :dev-dependencies [[swank-clojure "1.3.2"]]
  :jvm-opts ["-Xmx3g" "-server" "-Dfile.encoding=UTF-8"])
