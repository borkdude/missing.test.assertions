(ns missing.test.assertions
  (:require [clojure.test :refer [report
                                  #?(:clj *report-counters*)
                                  #?(:cljs get-current-env)]
             :as t]))

(defonce ^:private state (atom {}))

(defn- assertion-count []
  (let [rc #?(:clj @*report-counters*
              :cljs (:report-counters (get-current-env)))]
    (+ (get rc :pass 0)
       (get rc :fail 0)
       (get rc :error 0))))

(defn register!
  "Registers clojure.test/report :begin-test-var and :end-test-var
  methods, wrapping earlier registered methods."
  []
  (let [old-method (get-method report #?(:clj :begin-test-var
                                         :cljs [::t/default :begin-test-var]))]
    (defmethod report #?(:clj :begin-test-var
                         :cljs [::t/default :begin-test-var]) [m]
      (let [ret (when old-method (old-method m))]
        (swap! state assoc (-> m :var meta :name) (assertion-count))
        ret)))

  (let [old-method (get-method report #?(:clj :end-test-var
                                         :cljs [::t/default :end-test-var]))]
    (defmethod report #?(:clj :end-test-var
                         :cljs [::t/default :end-test-var]) [m]
      (let [test-name (-> m :var meta :name)
            ret (when old-method (old-method m))
            ac (assertion-count)]
        (when (= ac (get @state test-name))
          (binding #?(:clj [*out* *err*]
                      :cljs [*print-fn* *print-err-fn*])
            (println "WARNING: no assertions made in test" test-name)))
        ret))))

(register!)
