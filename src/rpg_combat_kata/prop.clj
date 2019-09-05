(ns rpg-combat-kata.prop
  (:import (java.util UUID)))

(defn create
  ([]
   (create nil))
  ([initial-values]
   (merge {:character/id (UUID/randomUUID) ; TODO: rename keyword?
           :prop/type :rock
           :character/health 1 ; TODO: rename keyword?
           :character/position {:x 0}} ; TODO: rename keyword?
          initial-values)))
