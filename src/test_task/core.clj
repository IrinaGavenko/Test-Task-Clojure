(ns test-task.core)

(defn my-frequencies
  [coll]
  (reduce
    #(update %1 %2 (fnil inc 0))
    {}
    coll))


(my-frequencies [1 1 2 1 3 4 2 2 3])
(my-frequencies '(1 1 2 3 1 2 3))
(my-frequencies #{1 2 3 4 5 6 7})