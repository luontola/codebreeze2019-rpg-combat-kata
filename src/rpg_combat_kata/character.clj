(ns rpg-combat-kata.character)

(def max-health 1000)

(def new {:character/health max-health
          :character/level 1})

(defn alive? [character]
  (pos? (:character/health character)))

(defn- change-health [health change]
  (max 0 (min max-health (+ health change))))

(defn attack [victim _attacker]
  (update victim :character/health change-health -1))

(defn heal [character health-points]
  (if (alive? character)
    (update character :character/health change-health health-points)
    character))
