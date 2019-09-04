(defproject rpg-combat-kata "0.1.0-SNAPSHOT"
  :description "RPG Combat Kata implementation in Clojure"
  :url "https://github.com/luontola/codebreeze2019-rpg-combat-kata"
  :license {:name "WTFPL"
            :url "http://www.wtfpl.net"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot rpg-combat-kata.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
