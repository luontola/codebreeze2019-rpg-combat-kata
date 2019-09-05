(ns rpg-combat-kata.coordinate-test
  (:require [clojure.test :refer :all]
            [rpg-combat-kata.coordinate :as coordinate]))

(deftest distance-test
  (is (= 0 (coordinate/distance {:x 0} {:x 0})))
  (is (= 2
         (coordinate/distance {:x 5} {:x 7})
         (coordinate/distance {:x 7} {:x 5}))))
