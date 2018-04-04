(ns rum-heroes.core
  (:require 
    [rum.core :as rum]
    [rum-heroes.world :as world]
    [rum-heroes.view.gridview :as gridview]))

(enable-console-print!)

;; all game state
(defonce grid-state (atom (world/init-background-grid)))
(defonce grid-overlay-state (atom (world/init-background-overlay)))
(defonce actors-state (atom (merge (world/init-army 5) (world/init-enemy-army 4))))

(defonce tile-hover-state (atom [ -1 -1 ]))

(rum/mount (gridview/grid-component 12 7 grid-state tile-hover-state)
  (. js/document (getElementById "world")))

(rum/mount [(gridview/grid-overlay-component grid-overlay-state)
            (gridview/grid-actors-component actors-state)]
  (. js/document (getElementById "world-overlay")))

(rum/defc hover-component < rum/reactive []
  [:h3 "hover: " (rum/react tile-hover-state)])

(rum/mount (hover-component)
  (. js/document (getElementById "baseUI")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
