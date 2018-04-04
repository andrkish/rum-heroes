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

;; get visual sprite from actors template
(defn get-actor-sprite [key]
  (get-in actors/actors-template [(keyword key) :visual] "empty"))

;; grid tile (cell) renderer component
(rum/defc grid-tile [x y grid-state]
  (let [cursor (rum/cursor-in grid-state [y x])]
  [:div.grid-back-tile 
    {:class (get-back-sprite @cursor)}]))

;; grid tile overlay renderer component
(rum/defc grid-overlay-tile [key state]
  (let [cursor (rum/cursor-in state [key])]
    [:div.grid-overlay {:class (get-overlay-sprite (get @cursor :visual)) 
                        :style { :left (grid/get-coord-x (get @cursor :posX))
                                  :top (grid/get-coord-y (get @cursor :posY))}}]))

;; full grid renderer component
(rum/defc grid-component [w h grid-state]
  [:div.grid 
  (for [y (range h)]
    [:div.grid-row
    (for [x (range w)]
      (grid-tile x y grid-state))])])

;; full overlay renderer component
(rum/defc grid-overlay-component [state]
  [ (for [k (keys @state)] 
      (grid-overlay-tile k state))])

;; actor render component
(rum/defc actor-component [key army]
  (let [cursor (rum/cursor-in army [key])]
    [:div.actor {:class (get-actor-sprite (get @cursor :template))
                 :style { :left (grid/get-coord-x (get-in @cursor [:pos :x]))
                           :top (grid/get-coord-y (get-in @cursor [:pos :y]))}}]))

;; full actors renderer component
(rum/defc grid-actors-component [army]
  [ (for [k (keys @army)]
      (actor-component k army))])