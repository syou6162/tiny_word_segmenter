# TinyWordSegmenter

Tiny word segmenter written in clojure. Currently, a simple document classifier with FOBOS was implemented. 

## Usage

### Usage of word segmenter

First of all, you have to install (Leiningen)[https://github.com/technomancy/leiningen]. To do this:

    cd ~/bin
    wget http://github.com/technomancy/leiningen/raw/stable/bin/lein
    chmod +x lein
    lein self-install
    # or you can use macports 
    sudo port install leiningen

And you have to fetch this repository from github.

    git clone git@github.com:TrainingCamp2012/TinyWordSegmenter.git

TinyWordSegmenter depends on several libraries. You can install these libraries:

    cd TinyWordSegmenter
    lein deps

You can train a model file for SVM with `lein run`. This will take a long time. After training the model, you can evaluate its model with `lein repl` and `((load-file "src/TinyWordSegmenter/decode_demo.clj"))` in repl.

### Usage of document classifier

Following is how to use the document classifier:

    wget http://www.csie.ntu.edu.tw/~cjlin/libsvmtools/datasets/binary/news20.binary.bz2
    bzip2 -d news20.binary.bz2 
    ruby shuffle news20.binary > tmp.txt
    head -n 15000 tmp.txt > train.txt; tail -n 4996 tmp.txt > test.txt
    lein run -m TinyWordSegmenter.news20binary --train-filename train.txt --test-filename test.txt --max-iter 10 --eta 1.0 --lambda 1.0

## License

Copyright (C) 2012 Yasuhisa Yoshida

Distributed under the Eclipse Public License, the same as Clojure.
