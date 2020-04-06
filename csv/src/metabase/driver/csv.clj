(ns metabase.driver.csv
  (:require [clj-time
             [coerce :as tcoerce]
             [format :as tformat]]
            [clojure.string :as str]
            [honeysql
             [core :as hsql]
             [format :as hformat]]
            [metabase
             [config :as config]
             [driver :as driver]]
            [metabase.driver
             [common :as driver.common]
             [sql :as sql]]
             [clojure.java.jdbc :as jdbc]
            [metabase.driver.sql-jdbc
             [connection :as sql-jdbc.conn]
             [sync :as sql-jdbc.sync]]
            [metabase.driver.sql.query-processor :as sql.qp]
            [metabase.util
             [date-2 :as du]
             [honeysql-extensions :as hx]]
            [schema.core :as s])
  (:import [java.sql Time Timestamp]
	   [java.util Calendar]))

(driver/register! :csv, :parent :sql-jdbc)

(def ^:private database-type->base-type
  (sql-jdbc.sync/pattern-based-database-type->base-type
    [
     [#"String"     :type/Text]
    ]))

(defn week-of-year
  [date]
	(let [cal (Calendar/getInstance)]
		(.setTime cal (.toDate date))
		(.get cal Calendar/WEEK_OF_YEAR)))

(defmethod sql.qp/date [:csv :day]            	[_ _ expr] (hsql/call :substring expr 1 10))
(defmethod sql.qp/date [:csv :month]            [_ _ expr] (hsql/call :substring expr 1 7))
(defmethod sql.qp/date [:csv :year]            	[_ _ expr] (hsql/call :year expr))
(defmethod sql.qp/date [:csv :second]           [_ _ expr] (hsql/call :substring expr 1 10))
(defmethod sql.qp/date [:csv :minute]           [_ _ expr] (hsql/call :substring expr 1 16))
(defmethod sql.qp/date [:csv :hour]            	[_ _ expr] (hsql/call :substring expr 1 13))
(defmethod sql.qp/date [:csv :hour-of-day]      [_ _ expr] (hsql/call :substring expr 1 13))
(defmethod sql.qp/date [:csv :week]             [_ _ expr] (:week-of-year expr))

(defmethod sql-jdbc.sync/database-type->base-type :csv [_ database-type]
  (database-type->base-type database-type))

(defmethod sql-jdbc.conn/connection-details->spec :csv [_ {:keys [csv separator charset advanced]
                                                              :or   {csv "arquivo.csv"}
                                                              :as   details}]
(merge {:classname   "org.relique.jdbc.csv.CsvDriver"
        :subprotocol "relique:csv"
        :subname     (str csv "?separator=" separator "&charset=" charset advanced)
 }
         (dissoc details :csv :separator :charset :advanced))

)
