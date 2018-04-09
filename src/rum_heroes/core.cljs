(ns rum-heroes.core
  (:require 
    [rum.core :as rum]
    [rum-heroes.world :as world]
    [rum-heroes.grid :as grid]
    [rum-heroes.view.uiview :as ui]
    [rum-heroes.view.gridview :as gridview]))

(enable-console-print!)

;; all game state
(defonce grid-state (atom (world/init-background-grid)))
(defonce grid-overlay-state (atom (world/init-background-overlay)))
(defonce actors-state (atom (into (world/init-army 5) (world/init-enemy-army 4))))

(defonce tile-hover-state (atom [ -1 -1 ]))
(defonce actor-hover-state (atom '()))
(defonce actor-selected-state (atom '()))

;; event handlers from view / ui 
(defn on-tile-hover [x y]
  (reset! tile-hover-state [x y])
  (when (grid/correct-cell? x y))
    (reset! actor-hover-state 
            (filter (fn [el] (= { :x x :y y } (get el :pos))) @actors-state)))

;; select own actor on click event handler
(defn select-actor []
  (when (not (empty? @actor-hover-state))
    (let [actor (first @actor-hover-state)]
      (reset! actor-selected-state actor))))

(defn on-tile-click [x y]
  (select-actor))

(rum/mount (gridview/grid-component 12 7 grid-state on-tile-hover on-tile-click)
  (. js/document (getElementById "world")))

(rum/mount [(gridview/grid-overlay-component grid-overlay-state)
            (gridview/grid-actors-component actors-state)]
  (. js/document (getElementById "world-overlay")))

(rum/mount [(gridview/grid-hover-component tile-hover-state)
            (gridview/actor-selected-component actor-selected-state)]
  (. js/document (getElementById "world-hover")))

(rum/mount [(ui/tile-hover-component tile-hover-state) 
            (ui/actor-selected-component actor-selected-state)
            (ui/actor-hover-component actor-hover-state)]
  (. js/document (getElementById "baseUI")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

