(ns rum-heroes.config.actors)

(defonce actors-good-pack [ "knight" "mage" "archer" "swordman" "berserk" ])
(defonce actors-evil-pack [ "knight" "mage" "archer" "swordman" "berserk" ])

;; helper function
;; todo: learn about macro? 
(defn create-template 
  [ key sprite hpMax ]
  (hash-map 
    key { :visual sprite 
          :hpMax hpMax }))

(def actors-template 
  (into {} 
    [(create-template :knight "actor-knight" 15)
     (create-template :mage "actor-mage" 5)
     (create-template :archer "actor-archer" 7)
     (create-template :swordman "actor-swordman" 10)
     (create-template :berserk "actor-berserk" 12)]))