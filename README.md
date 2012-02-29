# TinyWordSegmenter

Tiny word segmenter written in clojure. Although the attached model is trained with the Japanese corpora, you can train your own model with Chinese or Korean corpora.

## Usage

### Install Leiningen

First of all, you have to install (Leiningen)[https://github.com/technomancy/leiningen]. To do this:

    cd ~/bin
    wget http://github.com/technomancy/leiningen/raw/stable/bin/lein
    chmod +x lein
    lein self-install
    # or you can use macports 
    sudo port install leiningen

### How to train a model

You have to fetch this repository from github.

    git clone git@github.com:TrainingCamp2012/TinyWordSegmenter.git

TinyWordSegmenter depends on several libraries. You can install these libraries:

    cd TinyWordSegmenter
    lein deps

You can train a model file for SVM like this:

	lein run --file KyotoUniv.txt --mode train --eta 1.0 --lambda 1.0 --max-iter 100 --model-file weight.model --model fobos_clj.svm.SVM
	
This will take a long time.	If you prefer logistic regression rather than SVM, then type

	lein run --file KyotoUniv.txt --mode train --eta 1.0 --lambda 1.0 --max-iter 100 --model-file weight.model --model fobos_clj.logistic.Logistic

### How to decode characters using your trained model

After training the model, you can evaluate its model like this:
  
	lein trampoline run --mode decode --model-file weight.model
	
## License

Copyright (C) 2012 Yasuhisa Yoshida

Distributed under the Eclipse Public License, the same as Clojure.
