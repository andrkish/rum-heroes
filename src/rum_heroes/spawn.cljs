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
(defn get-rnd-actor-template [list]
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

(defn get-actions [template]
  { :moves (get template :moves)
    :attacks (get template :attacks) })

(defn spawn-from-template [x teamId tkey]
  (let [t (get actors/actors-template (keyword tkey))]
    { :pos (case teamId
                 0 (get-position x)
                 1 (get-enemy-position x))
      :actions (get-actions t)
      :teamId teamId
      :id (get-id)
      :hp (get t :hpMax -1)
      :template tkey }))

;; create actor entity 
(defn spawn-actor [x]
  (spawn-from-template x 0 (get-rnd-actor-template actors/actors-good-pack)))

;; create enemy actor entity
(defn spawn-enemy-actor [x]
  (spawn-from-template x 1 (get-rnd-actor-template actors/actors-evil-pack)))