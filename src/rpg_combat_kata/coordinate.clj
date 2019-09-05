(ns rpg-combat-kata.coordinate)

(defn distance [a b]
  (Math/abs ^int (- (:x a) (:x b))))
