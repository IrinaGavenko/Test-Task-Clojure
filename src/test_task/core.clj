(ns test-task.core)

(defn my-frequencies
  [coll]
  (reduce
    #(update %1 %2 (fnil inc 0))
    {}
    coll))