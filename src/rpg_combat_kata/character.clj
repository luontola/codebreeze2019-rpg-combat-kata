(ns rpg-combat-kata.character)

(def new {:character/health 1000
          :character/level 1})

(defn alive? [character]
  (pos? (:character/health character)))

(defn- deal-damage [health]
  (max 0 (dec health)))

(defn attack [_attacker victim]
  (update victim :character/health deal-damage))