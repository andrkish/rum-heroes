(ns rum-heroes.config.actors)

(defonce actors-good-pack [ "knight" "mage" "archer" "swordman" "berserk" ])
(defonce actors-evil-pack [ "knight" "mage" "archer" "swordman" "berserk" ])

;; helper function
;; todo: learn about macro? 
(defn create-template 
  [ key sprite hpMax damage range moves ]
  (hash-map 
    key { :visual sprite 
          :damage damage
          :range range
          :moves moves
          :hpMax hpMax }))

(def actors-template 
  (into {} 
    [(create-template :knight "actor-knight" 15 3 1 3)
     (create-template :mage "actor-mage" 5 3 2 1)
     (create-template :archer "actor-archer" 7 2 3 1)
     (create-template :swordman "actor-swordman" 10 2 1 2)
     (create-template :berserk "actor-berserk" 8 3 1 2)]))