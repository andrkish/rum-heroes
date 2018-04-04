(ns rum-heroes.config.actors)

(defonce actors-good-pack [ "knight" "mage" ])

(defonce actors-template 
  { :knight { :visual "actor-knight" 
              :hpMax 15 }
    :mage   { :visual "actor-mage" 
              :hpMax 5 }})