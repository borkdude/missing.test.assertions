(defproject borkdude/missing-test-assertions "0.0.1"
  :description "No assertion warnings. Naw!"
  :url "https://github.com/borkdude/missing-test-assertions"
  :license {:name "EPL-1.0"
            :url "http://opensource.org/licenses/eclipse-1.0.php"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :repl-options {:init-ns missing.test.assertions}
  ;; :monkeypatch-clojure-test false
  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass
                                    :sign-releases false}]])
