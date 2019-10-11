(ns metabase.driver.csv-test
  (:require [expectations :refer [expect]]
            [metabase.test.data.datasets :refer [expect-with-driver]]
            [metabase.test.util :as tu]))

(expect-with-driver :csv
  "UTC"
  (tu/db-timezone-id))
