package com.imcode.imcms;

import clojure.lang.RT;
import clojure.lang.Var;
import org.hibernate.SessionFactory;

import javax.sql.DataSource;

/**
 * Integration with scripted code.
 */
public class Script {

    public static final String TEST_SQL_SCRIPTS_HOME = "src/test/resources/sql";

    static {
        try {
            RT.load("com/imcode/imcms/runtime");

            RT.load("com/imcode/imcms/test/project");
            RT.load("com/imcode/imcms/test/project/db");
            RT.load("com/imcode/imcms/test/project/db/upgrade");

            RT.load("com/imcode/cljlib/db");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    public static Object callClojureFn(String ns, String fn, Object... args) {
//        try {
//            Var var = RT.var(ns, fn);
//
//            return var.applyTo(RT.seq(args));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    public static String getDBName() throws Exception {
        return (String)RT.var("com.imcode.imcms.test.project.db", "db-name")
               .invoke();
    }

    public static DataSource createDBDataSource(boolean autocommit) throws Exception {
        return (DataSource)RT.var("com.imcode.imcms.test.project.db", "create-ds")
               .invoke(autocommit);
    }

    public static void recreateDB() {
        try {
            RT.var("com.imcode.imcms.test.project.db", "recreate")
                .invoke();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void recreateDB(String... sqlScriptsPaths) {
        try {
            RT.var("com.imcode.imcms.test.project.db", "recreate")
                .invoke(createPaths(sqlScriptsPaths));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void recreateEmptyDB() {
        try {
            RT.var("com.imcode.imcms.test.project.db", "recreate-empty")
                .invoke();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public static void runDBScripts(String... sqlScriptsPaths) {
        try {
            RT.var("com.imcode.imcms.test.project.db", "run-scripts")
                .invoke(createPaths(sqlScriptsPaths));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }    
    

    public void initImcms() throws Exception {
        RT.var("com.imcode.imcms.test.project", "init-imcms")
            .invoke();
    }


    public static String[] createPaths(String[] scriptsNames) {
        String[] scriptsPaths = new String[scriptsNames.length];

        for (int i = 0; i < scriptsNames.length; i++) {
            scriptsPaths[i] = TEST_SQL_SCRIPTS_HOME + "/" + scriptsNames[i];
        }

        return scriptsPaths;
    }


    public static SessionFactory createHibernateSessionFactory(Class... annotatedClasses) {
        return createHibernateSessionFactory(annotatedClasses, new String[0]);
    }


    public static SessionFactory createHibernateSessionFactory(Class[] annotatedClasses, String... xmlFiles) {

        try {
            return (SessionFactory)RT.var("com.imcode.imcms.test.project.db", "create-hibernate-sf")
                .invoke(annotatedClasses, xmlFiles);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}