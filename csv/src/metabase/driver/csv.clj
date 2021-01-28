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

(def ^:private database-type->base-type
  (sql-jdbc.sync/pattern-based-database-type->base-type
    [
     [#"String"         :type/Text]
     [#"Asciistream"    :type/Text]
     [#"BigDecimal"     :type/Decimal]
     [#"Boolean"        :type/Boolean]
     [#"Byte"           :type/Byte]
     [#"Date"           :type/Date]
     [#"Double"         :type/Float]
     [#"Float"          :type/Float]
     [#"Integer"        :type/Integer]
     [#"Long"           :type/BigInteger]
     [#"Short"          :type/Integer]
     [#"String"         :type/String]
     [#"Timestamp"      :type/DateTime]
     [#"Time"           :type/Time]
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


(defn is-http [path]  (if (clojure.string/starts-with? (clojure.string/lower-case path) "http") true false))

(defmethod sql-jdbc.conn/connection-details->spec :csv [_ {:keys [csv separator charset advanced]
                                                              :or   {csv "arquivo.csv"}
                                                              :as   details}]

(def strHttp (if (is-http csv) ":class:br.markenson.com.csvjdbc4metabase.readers.HttpCSVReader" ""))

(def customBaseUrl (if (is-http csv) (str "&customBaseUrl=" csv) ""))

(merge {:classname   "org.relique.jdbc.csv.CsvDriver"
        :subprotocol (str "relique:csv" strHttp)
        :subname     (str (if (is-http csv) "" csv) "?separator=" separator "&charset=" charset customBaseUrl advanced)
 }
         (dissoc details :csv :separator :charset :customBaseUrl :advanced))  
  
)
