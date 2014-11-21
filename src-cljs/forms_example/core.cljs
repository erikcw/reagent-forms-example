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
   [:h3 "Enter a status code into the form field, the select field should update."]
   (row "Enter a status code" [:input.form-control {:field :text :id :example :placeholder "Input a valid status key (A, P, or X)"}])
   (row "Status" [:select.form-control {:field :list :id :status}
                 [:option {:key "A"} "Active"]
                 [:option {:key "P"} "Paused"]
                 [:option {:key "X"} "Archived"]])])

(defn page []
  (let [doc (atom {:status "P"})]
    (fn []
      [:div [:h1 "Status Page"]
       [edn->hiccup @doc]
       [bind-fields status-form doc (fn [id value doc]
                                      (.log js/console "doc before event modification" (clj->js doc))
                                      (if (not (empty? (:example doc)))
                                        (let [new-doc (assoc doc :status (:example doc))]
                                          (.log js/console "doc after event modification" (clj->js doc))
                                          new-doc)
                                        doc))]])))

(reagent/render-component [page] (.getElementById js/document "app"))
