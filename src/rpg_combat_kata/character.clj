(ns rpg-combat-kata.character
  (:import (java.util UUID)))

(def max-health 1000)

(defn new []
  {:character/id (UUID/randomUUID)
   :character/health max-health
   :character/level 1})

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
    (change-health target -1)
    target))

(defn heal [target healer]
  (if (and (alive? target)
           (same? target healer))
    (change-health target 1)
    target))
