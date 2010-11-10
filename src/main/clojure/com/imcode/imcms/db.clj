(ns 
  #^{:doc "Database utils."}
  com.imcode.imcms.db
  (:require
    (clojure.contrib [sql :as sql])
    (clojure [string :as str])
    (clojure.java [io :as io]))
  
  (:use
    [clojure.contrib.except :only (throw-if)]
    [clojure.contrib.def :only (defvar)]
    [com.imcode.imcms.misc :only (dump)])
  
  (:import
    (org.apache.commons.dbcp BasicDataSource) 
    (com.ibatis.common.jdbc ScriptRunner)))


(defvar *execute-script-statements* true
  "Controls if script's statement will be executed by run-script fn.")


(defn create-ds
  "Creates DBCP datasource."
  [driver-class-name url username password]
  (doto (BasicDataSource.)
    (.setDriverClassName driver-class-name)
    (.setUsername username)
    (.setPassword password)
    (.setUrl url)))


(defn create-spec
  "Creates db-spec from datasource which is used by clojure.contrib.sql fns."
  [ds]
  {:datasource ds})


(defn create-h2-mem-spec 
  ([]
     (create-h2-mem-spec ""))

  ([name & params]
    {:classname "org.h2.Driver"
     :subprotocol "h2"
     :subname (str "mem:" name (str/join ";" params))}))


(defn- print-script-info
  "Prints SQL script name and lines count.
  This fn is called when *execute-script-statements* is set to false."
  [script script-lc]
  (println (format
    "WARNING! Script [%s, %s lines] statements will not be executed: %s is set to false ."
    script script-lc #'*execute-script-statements*)))


(defn run-script
  "Runs sql script using existing connection."
  ([connection script]
    (run-script connection script false true))

  ([connection script autocommit stop-on-error]
    (with-open [reader (io/reader script)]
      (if *execute-script-statements*
        (doto (ScriptRunner. connection autocommit stop-on-error)
          (.runScript reader))

        (print-script-info script (count (line-seq reader)))))))


(defn run-scripts
  "Run sql scripts."
  ([spec scripts]
    (run-scripts spec nil scripts))

  ([spec schema-name scripts]
    (sql/with-connection spec
      (sql/transaction
        (when schema-name
          (sql/do-commands
            (format "use %s" schema-name)))

        (doseq [script scripts]
          (run-script (sql/connection) script))))))

  
(defn recreate
  "Recreates databse."
  [spec schema-name scripts]
  (sql/with-connection spec
    (sql/transaction
      (sql/do-commands
        (format "drop database if exists %s" schema-name)
        (format "create database %s" schema-name)
        (format "use %s" schema-name))

      (doseq [script scripts]
        (run-script (sql/connection) script)))))


(defn delete
  "Deletes database."
  [spec schema-name]
  (sql/with-connection spec
    (sql/do-commands
      (format "drop database if exists %s" schema-name))))


(defn metadata 
  "Returns database metadata"
  [spec]
  (sql/with-connection spec
    (.getMetaData (sql/connection))))


(defn print-metadata [spec]
  (dump (metadata spec)))