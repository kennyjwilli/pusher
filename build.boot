(def project 'pusher)
(def version "0.1.1")

(set-env!
  :source-paths #{"src" "test"}
  :resource-paths #{"src" "test"}
  :asset-paths #{"html"}
  :dependencies '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.0" :scope "test"]
                  [com.cemerick/piggieback "0.2.1" :scope "test"]
                  [weasel "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                  [adzerk/boot-reload "0.4.8" :scope "test"]
                  [pandeiro/boot-http "0.7.3" :scope "test"]
                  [cljsjs/boot-cljsjs "0.5.1" :scope "test"]
                  ;;project deps
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.8.51"]
                  [com.pusher/pusher-http-java "1.0.0"]
                  [com.pusher/pusher-java-client "1.1.3"]
                  [cheshire "5.6.1"]
                  [cljsjs/pusher "3.0.0-0"]
                  [funcool/beicon "1.4.0"]]
  :compiler-options {:compiler-stats true})

(require
  '[adzerk.boot-cljs :refer :all]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer :all]
  '[pandeiro.boot-http :refer :all]
  '[cljsjs.boot-cljsjs :refer :all])


(task-options!
  pom {:project     project
       :version     version
       :description "FIXME: write description"
       :url         "https://github.com/kennyjwilli/pusher"
       :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask web-dev
         "Developer workflow for web-component UX."
         []
         (comp
           (serve :dir "target/")
           (watch)
           (cljs-repl)
           (speak)
           (reload)
           (cljs)))

;boot watch pom -p pusher -v 0.1.0-SNAPSHOT jar install