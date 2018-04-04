(ns rum-heroes.view.gridview
  (:require 
    [rum.core :as rum]
    [rum-heroes.config.actors :as actors]
    [rum-heroes.grid :as grid]))

;; helpers method for getting sprite
(defn get-back-sprite [n]
  (case n
    0 "grass-sprite-1"
    1 "grass-sprite-2"
    2 "grass-sprite-3"))

(defn get-overlay-sprite [n]
  (case n
    0 "forest-overlay-1"
    1 "forest-overlay-2"
    2 "forest-overlay-3"))

;; grid tile (cell) renderer component
(rum/defc grid-tile [x y grid-state on-tile-hover]
  (let [cursor (rum/cursor-in grid-state [y x])]
  [:div.grid-back-tile 
    {:class (get-back-sprite @cursor)
     :on-mouse-over (fn [_] (on-tile-hover x y))}]))

;; grid tile overlay renderer component
(rum/defc grid-overlay-tile [key state]
  (let [cursor (rum/cursor-in state [key])]
    [:div.grid-overlay {:class (get-overlay-sprite (get @cursor :visual)) 
                        :style { :left (grid/get-coord-x (get @cursor :posX))
                                  :top (grid/get-coord-y (get @cursor :posY))}}]))

;; full grid renderer component
(rum/defc grid-component [w h grid-state on-tile-hover]
  [:div.grid { :on-mouse-out (fn [_] (on-tile-hover -1 -1))}
  (for [y (range h)]
    [:div.grid-row
    (for [x (range w)]
      (grid-tile x y grid-state on-tile-hover))])])

;; full overlay renderer component
(rum/defc grid-overlay-component [state]
  [ (for [k (keys @state)] 
      (grid-overlay-tile k state))])

;; helper method 
(defn get-actor-style [cursor]
  (let [posX (grid/get-coord-x (get-in @cursor [:pos :x]))
        posY (grid/get-coord-y (get-in @cursor [:pos :y]))]
    (hash-map :left posX :top posY )))

(defn get-hover-style [x y]
  (let [posX (grid/get-coord-x x)
        posY (grid/get-coord-y y)]
    (hash-map :left posX :top posY)))

;; get visual sprite from actors template
(defn get-actor-class [key teamId]
  (let [actor-sprite (get-in actors/actors-template [(keyword key) :visual] "empty")]
    (str "actor " (when (> teamId 0) "actor-enemy ") actor-sprite)))

(defn get-actor-hp-class [teamId]
  (case teamId
    0 "green" 
    1 "red"))
 
;; actor render component
(rum/defc actor-component [index army]
  (let [cursor (rum/cursor-in army [index])]
    [:div.actor-container { :style (get-actor-style cursor)}
      [:div { :class (get-actor-class (get @cursor :template) (get @cursor :teamId))}]
      [:div.actor-ui [ :span.actor-ui-hp { :class (get-actor-hp-class (get @cursor :teamId)) } (get @cursor :hp ) ]]]))

;; full actors renderer component
(rum/defc grid-actors-component [army]
  (map-indexed (fn [i x] [(actor-component i army)]) @army))

;; tile hover component
(rum/defc grid-hover-component < rum/reactive [state]
  (let [cursor (rum/cursor-in state [] )
        x (get @state 0)
        y (get @state 1)]
    (when (and (rum/react cursor) (grid/correct-cell? x y))
      [:div.grid-hover { :style (get-hover-style x y)}])))
