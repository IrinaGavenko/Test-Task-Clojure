(require '[clojure.string :as str])

(ns test-task.core)

(defn get-domain
  [link]
  (nth (str/split link #"/") 2))

(defn my-frequencies
  [coll]
  (reduce
    #(update %1 %2 (fnil inc 0))
    {}
    coll))

(defn get-statistics
  [coll]
  (->>
    coll
    (set)
    (map get-domain)
    frequencies))
