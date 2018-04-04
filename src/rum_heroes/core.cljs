(ns rum-heroes.core
  (:require 
    [rum.core :as rum]
    [rum-heroes.world :as world]
    [rum-heroes.grid :as grid]
    [rum-heroes.view.gridview :as gridview]))

(enable-console-print!)

;; all game state
(defonce grid-state (atom (world/init-background-grid)))
(defonce grid-overlay-state (atom (world/init-background-overlay)))
(defonce actors-state (atom (into (world/init-army 5) (world/init-enemy-army 4))))

(defonce tile-hover-state (atom [ -1 -1 ]))
(defonce actor-hover-state (atom '()))

;; event handlers from view / ui 
(defn on-tile-hover [x y]
  (reset! tile-hover-state [x y])
  (when (grid/correct-cell? x y))
    (reset! actor-hover-state 
            (filter (fn [el] (= { :x x :y y } (get el :pos))) @actors-state)))

(defn on-tile-click [x y]
  ())

(rum/mount (gridview/grid-component 12 7 grid-state on-tile-hover)
  (. js/document (getElementById "world")))

(rum/mount [(gridview/grid-overlay-component grid-overlay-state)
            (gridview/grid-actors-component actors-state)]
  (. js/document (getElementById "world-overlay")))

(rum/mount (gridview/grid-hover-component tile-hover-state)
  (. js/document (getElementById "world-hover")))


(rum/defc hover-component < rum/reactive []
  [:h3 "hover: " (rum/react tile-hover-state)])

(rum/defc actor-hover-component < rum/reactive [actor]
  (let [cursor (rum/cursor-in actor [])]
    (when (and (rum/react cursor) (not (empty? @cursor)))
      (let [a (first @cursor)]
        [:div.hover-unit
          [ :div "HOVER UNIT "]
          [ :div "template: " (get a :template)]
          [ :div "teamId: " (get a :teamId)]]))))

(rum/mount [(hover-component) (actor-hover-component actor-hover-state)]
  (. js/document (getElementById "baseUI")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

