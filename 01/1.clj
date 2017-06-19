(require '[clojure.string :as str])

(defn process-instruction [instruction]
  (let [[direction length] (rest (re-matches #"^([RL])(\d+)$" instruction))]
    ;; (println direction length)
    [(keyword direction) (Integer/parseInt length)]))

(defn rotate [theta vector]
  (let [[x y] vector]
    [(- (* x (Math/cos theta)) (* y (Math/sin theta)))
      (+ (* x (Math/sin theta)) (* y (Math/cos theta)))]))
(def left-rotate (partial rotate (/ Math/PI 2.)))
(def right-rotate (partial rotate (- (/ Math/PI 2.))))

(defn scalar-vector-multiply [scalar vector]
  (let [[x y] vector]
    [(* scalar x) (* scalar y)]))

(defn vector-add [vector-1 vector-2]
  (let [ [[x-1 y-1] [x-2 y-2]] [vector-1 vector-2]]
    [(+ x-1 x-2) (+ y-1 y-2)]))

(defn load-instructions [filename]
  (str/split 
    (str/trim-newline (slurp filename))
    #", "))

(println (int (Math/round (reduce + ((reduce (fn [accum x]
    (let [orientation (accum :orientation)
      turn-function (if (= :L (x 0)) left-rotate right-rotate)
      new-orientation (turn-function orientation)
      displacement (accum :displacement)
      move-amount (x 1)
      new-displacement (vector-add
        displacement
        (scalar-vector-multiply move-amount new-orientation))]
      ;; (println x)
      ;; (println orientation)
      ;; (println displacement)
      ;; (println new-displacement)
      ;; (println new-orientation)
      ;; (println move-amount)
      ;; (println accum)
      ;; (println "")
      (assoc accum
        :orientation new-orientation
        :displacement new-displacement)))
    {:orientation [0 1] :displacement [0 0]}
    (map process-instruction (load-instructions "./data/1.txt")))
    :displacement)))))
