(ns rpg-combat-kata.character
  (:import (java.util UUID)))

(def max-health 1000)

(defn create
  ([]
   (create nil))
  ([initial-values]
   (merge {:character/id (UUID/randomUUID)
           :character/health max-health
           :character/level 1
           :character/dps 10
           :character/healing-power 10}
          initial-values)))

(defn alive? [character]
  (pos? (:character/health character)))

(defn same? [character-1 character-2]
  (= (:character/id character-1)
     (:character/id character-2)))

(defn- change-health [character change]
  (update character :character/health (fn [health]
                                        (max 0 (min max-health (+ health change))))))

(defn attack [target attacker]
  (if (not (same? target attacker))
    (let [level-diff (- (:character/level attacker)
                        (:character/level target))
          multiplier (cond
                       (<= 5 level-diff) 1.5
                       (<= level-diff -5) 0.5
                       :else 1)
          dps (int (* multiplier (:character/dps attacker)))]
      (change-health target (- dps)))
    target))

(defn heal [target healer]
  (if (and (alive? target)
           (same? target healer))
    (change-health target (:character/healing-power healer))
    target))
