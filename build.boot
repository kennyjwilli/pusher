(set-env!
  :source-paths #{"src" "test"}
  :resource-paths #{"src" "test" "scss" "bower_components"}
  :asset-paths #{"html" "bower_components"}
  :wagons '[[s3-wagon-private "1.1.2"]]
  :repositories [["clojars" "http://clojars.org/repo/"]
                 ["maven-central" "http://repo1.maven.org/maven2/"]]
  :dependencies '[[adzerk/boot-cljs "1.7.228-1" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.0" :scope "test"]
                  [com.cemerick/piggieback "0.2.1" :scope "test"]
                  [weasel "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                  [adzerk/boot-reload "0.4.4" :scope "test"]
                  [pandeiro/boot-http "0.7.0" :scope "test"]
                  [cljsjs/boot-cljsjs "0.5.1" :scope "test"]]
  :compiler-options {:compiler-stats true})

(require
  '[adzerk.boot-cljs :refer :all]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer :all]
  '[pandeiro.boot-http :refer :all]
  '[cljsjs.boot-cljsjs :refer :all])

(def project (let [p (read-string (slurp "project.clj"))]
               (into {:project-name (nth p 1)
                      :version      (nth p 2)}
                     (map vec (->> p (drop 3) (partition 2))))))

(set-env! :dependencies #(-> project :dependencies vec (into %)))


(task-options!
  pom {:project     (:project-name project)
       :version     (:version project)
       :description (:description project)
       :url         (:url project)
       :scm         (:scm project)
       :license     {(:name (:license project)) (:url (:license project))}})

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