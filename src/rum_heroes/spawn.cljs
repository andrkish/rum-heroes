(ns rum-heroes.spawn
  (:require
    [rum-heroes.config.actors :as actors]
    [rum-heroes.grid :as grid]))

;; setup id counter
(defonce id-counter (atom 0))
(defn get-id [] 
  (swap! id-counter inc)
  @id-counter)

;; sprites info
(def ^:const background-sprite-count 3)
(def ^:const overlay-sprite-count 3)

;; generate default background tile graphic
(defn gen-background-tile [x]
  (rand-int background-sprite-count))

;; generate default overlay tile graphic
(defn gen-overlay-tile []
  (rand-int overlay-sprite-count))

;; create overlay entity { k1 : { posX, posY, visual }}
(defn spawn-overlay [x]
  (hash-map (keyword (str "overlay" x))
            { :posX (grid/get-cell-x x)
              :posY (grid/get-cell-y x)
              :visual (gen-overlay-tile)}))

;; get random actor template
(defn get-actor-template [list]
  (-> (count list)
      (rand-int)
      (#(get list %1))))

(defn get-position [n]
  (case n
    0 { :x 1 :y 1 }
    1 { :x 0 :y 2 }
    2 { :x 1 :y 3 }
    3 { :x 0 :y 4 }
    4 { :x 1 :y 5 }
    5 { :x 4 :y 2 }))

(defn get-enemy-position [n]
  (case n
    0 { :x (- grid/grid-width 1) :y 1 }
    1 { :x (- grid/grid-width 1) :y 2 }
    2 { :x (- grid/grid-width 1) :y 3 }
    3 { :x (- grid/grid-width 1) :y 4 }
    4 { :x (- grid/grid-width 1) :y 5 }
    5 { :x 6 :y 2 }))

;; create actor entity 
(defn spawn-actor [x]
  (let [template (get-actor-template actors/actors-good-pack)]
    { :pos (get-position x)
      :teamId 0
      :id (get-id)
      :hp (get-in actors/actors-template [(keyword template) :hpMax] -1)
      :template template }))

;; create enemy actor entity
(defn spawn-enemy-actor [x]
  (let [template (get-actor-template actors/actors-evil-pack)]
    { :pos (get-enemy-position x)
      :teamId 1
      :id (get-id)
      :hp (get-in actors/actors-template [(keyword template) :hpMax] -1)
      :template template }))