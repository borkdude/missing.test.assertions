(ns missing.test.assertions
  (:require [clojure.test :refer [report
                                  #?(:clj *report-counters*)
                                  #?(:cljs get-current-env)]
             :as t]))

(defonce ^:private state (atom {}))

(defn- report-counters []
  #?(:clj @*report-counters*
     :cljs (:report-counters (get-current-env))))

(defn- assertion-count []
  (let [rc (report-counters)]
    (+ (get rc :pass 0)
       (get rc :fail 0)
       (get rc :error 0))))

(defn register!
  "Registers clojure.test/report :begin-test-var and :end-test-var
  methods, wrapping earlier registered methods. Accepts optional options map.
  Options:
    - `:throw?`: throw exception instead of printing to STDOUT."
  ([] (register! nil))
  ([{:keys [:throw?]}]
   (let [old-method (get-method report #?(:clj :begin-test-var
                                          :cljs [::t/default :begin-test-var]))]
     (defmethod report #?(:clj :begin-test-var
                          :cljs [::t/default :begin-test-var]) [m]
       (let [ret (when old-method (old-method m))]
         (swap! state assoc (-> m :var meta :name) (assertion-count))
         ret)))

   (let [old-method (get-method report #?(:clj :end-test-var
                                          :bb :end-test-var     
                                          :cljs [::t/default :end-test-var]))]
     (defmethod report #?(:clj :end-test-var
                          :bb :end-test-var
                          :cljs [::t/default :end-test-var]) [m]
       (let [test-name (-> m :var meta :name)
             ret (when old-method (old-method m))
             ac (assertion-count)]
         (when (= ac (get @state test-name))
           (binding #?(:clj [*out* *err*]
                       :cljs [*print-fn* *print-err-fn*])
             (if throw?
               (throw (ex-info "No assertions made in test." {:report-counters (report-counters)}))
               (println "WARNING: no assertions made in test" test-name))))
         ret)))))

(register!)
