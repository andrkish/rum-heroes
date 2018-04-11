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
(rum/defc grid-tile [x y grid-state on-tile-hover on-tile-click]
  (let [cursor (rum/cursor-in grid-state [y x])]
    [:div.grid-back-tile 
      {:class (get-back-sprite @cursor)
       :on-click (fn [_] (on-tile-click x y))
       :on-mouse-over (fn [_] (on-tile-hover x y))}]))

;; grid tile overlay renderer component
(rum/defc grid-overlay-tile [key state]
  (let [cursor (rum/cursor-in state [key])]
    [:div.grid-overlay {:class (get-overlay-sprite (get @cursor :visual)) 
                        :style { :left (grid/get-coord-x (get @cursor :posX))
                                  :top (grid/get-coord-y (get @cursor :posY))}}]))

;; full grid renderer component
(rum/defc grid-component [w h grid-state on-tile-hover on-tile-click]
  [:div.grid { :on-mouse-out (fn [_] (on-tile-hover -1 -1))}
    (for [y (range h)]
      [:div.grid-row
      (for [x (range w)]
        (grid-tile x y grid-state on-tile-hover on-tile-click))])])

;; full overlay renderer component
(rum/defc grid-overlay-component [state]
  [ (for [k (keys @state)] 
      (grid-overlay-tile k state))])

;; helper methods
(defn get-position-style [cursor]
  (let [posX (grid/get-coord-x (get-in @cursor [:pos :x]))
        posY (grid/get-coord-y (get-in @cursor [:pos :y]))]
    (hash-map :left posX :top posY )))

(defn get-position [x y]
  (let [posX (grid/get-coord-x x)
        posY (grid/get-coord-y y)]
    (hash-map :left posX :top posY)))

(defn get-hover-style [x y]
  (let [posX (grid/get-coord-x x)
        posY (grid/get-coord-y y)]
    (hash-map :left posX :top posY)))

;; get visual sprite from actors template
(defn get-actor-class [key teamId hp]
  (let [actor-sprite (get-in actors/actors-template [(keyword key) :visual] "empty")]
    (str "actor " 
         (when (<= hp 0) "actor-dead ")
         (when (> teamId 0) "actor-enemy ") 
         actor-sprite)))

(defn get-actor-hp-class [teamId]
  (case teamId
    0 "team0" 
    1 "team1"))
 
;; actor render component
(rum/defc actor-component < rum/reactive [index army]
  (let [cursor (rum/cursor-in army [index])]
    (when (rum/react cursor)
      [:div.actor-container { :style (get-position-style cursor)}
        [:div { :class (get-actor-class (get @cursor :template) 
                                        (get @cursor :teamId)
                                        (get @cursor :hp))}]
        (when (> (get @cursor :hp) 0)
          [:div.actor-ui
            [ :span.actor-ui-hp { :class (get-actor-hp-class (get @cursor :teamId)) }
                                  (get @cursor :hp ) ]])])))

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

;; actor selected component (state = selected actor atom)
(rum/defc actor-selected-component < rum/reactive [state]
  (let [cursor (rum/cursor-in state [] )]
    (when (and (rum/react cursor) (not (empty? @cursor)))
      [:div.actor-selected { :style (get-position-style cursor)}])))

;; render available moves (state = moves state atom)
(rum/defc moves-render-component < rum/reactive [state]
  (let [cursor (rum/cursor-in state [])]
    (when (and (rum/react cursor) (not (empty? @cursor)))
      [:div 
        (for [x @cursor]
          [:div.move-overlay { :style (get-position (get x 0) (get x 1))}])])))

;; render available targets (state = targets state atom)
(rum/defc targets-render-component < rum/reactive [state]
  (let [cursor (rum/cursor-in state [])]
    (when (and (rum/react cursor) (not (empty? @cursor)))
        (for [x @cursor]
          (let [posX (get-in x [:pos :x])
                posY (get-in x [:pos :y])]
            [:div.enemy-target { :style (get-position posX posY) }])))))