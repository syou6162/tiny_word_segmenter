(defproject TinyWordSegmenter "0.0.5"
  :description "Tiny word segmenter written in clojure"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
		 [fobos_clj "0.0.1"]]
  :dev-dependencies [[swank-clojure "1.3.2"]]
  :jvm-opts ["-Xmx20g" "-server" "-Dfile.encoding=UTF-8"]
  :main TinyWordSegmenter.core)
