(ns rpg-combat-kata.character
  (:import (java.util UUID)))

(def max-health 1000)

(defn new []
  {:character/id (UUID/randomUUID)
   :character/health max-health
   :character/level 1})

(defn alive? [character]
  (pos? (:character/health character)))

(defn- change-health [health change]
  (max 0 (min max-health (+ health change))))

(defn attack [target attacker]
  (if (= (:character/id target)
         (:character/id attacker))
    target
    (update target :character/health change-health -1)))

(defn heal [character health-points]
  (if (alive? character)
    (update character :character/health change-health health-points)
    character))
