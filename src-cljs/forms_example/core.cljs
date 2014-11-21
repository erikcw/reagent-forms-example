(ns forms-example.core
  (:require [json-html.core :refer [edn->hiccup]]
            [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields init-field value-of]]))

(defn row [label input]
  [:div.row
   [:div.col-md-2 [:label label]]
   [:div.col-md-5 input]])

(def status-form
  [:div
   (row "Status" [:select.form-control {:field :list :id :status}
                 [:option {:key "A"} "Active"]
                 [:option {:key "P"} "Paused"]
                 [:option {:key "X"} "Archived"]])])

(defn page []
  (let [doc (atom {:status "P"})]
    (fn []
      [:div [:h1 "Status Page"]
       [:div
       [:button
        {:on-click (fn [e]
                     (.info js/console "archive button clicked")
                     (swap! doc :status "X"))}
        "Archive"]
       [:button
        {:on-click (fn [e]
                     (.info js/console "pause button clicked")
                     (swap! doc :status "P"))}
        "Pause"]

       [:button
        {:on-click (fn [e]
                     (.info js/console "activate button clicked")
                     (swap! doc :status "A"))}
        "Activate"]
        [:p (str @doc)]]
       [edn->hiccup @doc]
       [bind-fields status-form doc]])))

(reagent/render-component [page] (.getElementById js/document "app"))
