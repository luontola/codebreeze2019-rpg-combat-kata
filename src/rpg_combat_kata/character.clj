(ns rpg-combat-kata.character)

(def new {:character/health 1000
          :character/level 1})

(defn alive? [character]
  (pos? (:character/health character)))

(defn- change-health [health change]
  (max 0 (+ health change)))

(defn attack [_attacker victim]
  (update victim :character/health change-health -1))

(defn heal [character health-points]
  (if (alive? character)
    (update character :character/health change-health health-points)
    character))
