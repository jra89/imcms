package imcode.server;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Category;

class DBConnect {

    private Connection con = null;                 // The JDBC Connection
    private ResultSet rs = null;		    // The JDBC ResultSet
    private ResultSetMetaData rsmd = null;	    // The JDBC ResultSetMetaData
    private PreparedStatement ps;
    private CallableStatement cs = null;	    // The JDBC CallableStatement
    private String strSQLString = "";		    // SQL query-string
    private String strProcedure = "";		    // Procedure
    private String[] meta_data;       // Meta info
    private boolean trimStr = true;
    private int columnCount;                       // Column count

    private static Category log = Category.getInstance("server");

    // constructor
    DBConnect(imcode.server.InetPoolManager conPool) {
        try {
            con = conPool.getConnection();
        } catch (SQLException e) {
            log.error("Failed to get connection from connectionpool.",e);
        }
    }

    /**
     * <p>Execute a database query.
     */
    Vector executeQuery() {

        Vector results = new Vector();

        // Execute SQL-string
        try {
            ps.executeQuery();
            rs = ps.getResultSet();
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
            meta_data = new String[columnCount];
            for (int i = 0; i < columnCount;) {
                meta_data[i] = rsmd.getColumnLabel(++i);
            }

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String s = rs.getString(i);
                    if (s == null) {
                        s = "";
                    } else if (trimStr) {
                        s = s.trim();
                    }
                    results.addElement(s);
                }
            }

            rs.close();
            ps.close();

        } catch (SQLException ex) {
            log.error("Error in executeQuery()",ex);
        } finally {
            closeConnection();
        }

        return results;
    }

    /**
     * <p>Update databasequery.
     */
    int executeUpdateQuery() {
        // Execute SQL-string
        try {
            int result = ps.executeUpdate();
            ps.close();
            return result;
        } catch (SQLException ex) {
            log.error("Error in executeUpdateQuery()", ex);
            return -1;
        } finally {
            closeConnection();
        }
    }

    /**
     * <p>Execute a database procedure.
     */
    private Vector executeProcedure() {

        Vector results = new Vector();
        try {
            if (cs == null) {
                throw new NullPointerException("DBConnect.executeProcedure() cs == null");
            }
            rs = cs.executeQuery();
            if (rs == null) {
                throw new NullPointerException("DBConnect.executeProcedure() rs == null");
            }
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();

            meta_data = new String[columnCount];
            for (int i = 0; i < columnCount;) {
                meta_data[i] = rsmd.getColumnLabel(++i);
            }
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String s = rs.getString(i);
                    if (null != s && trimStr) {
                        results.addElement(s.trim());
                    } else {
                        results.addElement(s);
                    }
                }
            }

            rs.close();
            cs.close();
        } catch (SQLException ex) {
            log.error("Error in executeProcedure().",ex);
        } finally {
            closeConnection();
        }
        return results;
    }


    /**
     * <p>Update database procedure.
     * 
     * @return updatecount or -1 if error
     */
    private int executeUpdateProcedure() {
        int res = 0;
        try {
            res = cs.executeUpdate();
            cs.close();
        } catch (SQLException ex) {
            log.error("Error in executeUpdateProcedure().", ex);
        } finally {
            closeConnection();
        }
        return res;
    }


    /**
     * <p>Get metadata.
     */
    String[] getMetaData() {
        return meta_data;
    }


    /**
     * <p>Get columncount.
     */
    int getColumnCount() {
        return columnCount;
    }


    /**
     * <p>Close a database connection.
     */
    private void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            log.error("Failed to close connection.", ex);
        }
        con = null;
    }


    /**
     * <p>Set procedure.
     */
    private void setProcedure(String procedure, String params[]) {
        if (procedure == null) {
            throw new NullPointerException("DBConnect.setProcedure() procedure == null");
        }
        if (params == null) {
            throw new NullPointerException("DBConnect.setProcedure() param == null");
        }
        strProcedure = "{call " + procedure + "}";
        prepareProcedureStatementAndSetParameters(params);
    }

    private void prepareProcedureStatementAndSetParameters(String[] params) {
        try {
            cs = con.prepareCall(strProcedure);
            for (int i = 0; i < params.length; ++i) {
                cs.setString(i + 1, params[i]);
            }
        } catch (SQLException ex) {
            log.error("Error in prepareProcedureStatementAndSetParameters()", ex);
        }
    }

    /**
     * <p>Set trim. true = trim strings, false = do not trim strings.
     */
    void setTrim(boolean status) {
        trimStr = status;
    }


    int executeUpdateProcedure(String procedure, String[] params) {
        procedure = addQuestionMarksToProcedureCall(procedure, params);
        setProcedure(procedure, params);
        return executeUpdateProcedure();
    }

    Vector executeProcedure(String procedure, String[] params) {
        procedure = addQuestionMarksToProcedureCall(procedure, params);
        setProcedure(procedure, params);
        return executeProcedure();
    }

    private static String addQuestionMarksToProcedureCall(String procedure, String[] params) {
        if (params.length > 0) {
            StringBuffer procedureBuffer = new StringBuffer(procedure);
            procedureBuffer.append(" ?");
            for (int i = 1; i < params.length; ++i) {
                procedureBuffer.append(",?");
            }
            procedure = procedureBuffer.toString();
        }
        return procedure;
    }

    void setSQLString(String sqlStr, String[] params) {
        strSQLString = sqlStr;
        prepareQueryStatementAndSetParameters(params);
    }

    private void prepareQueryStatementAndSetParameters(String[] params) {
        try {
            ps = con.prepareStatement(strSQLString);
            for (int i = 0; i < params.length; ++i) {
                ps.setString(i + 1, params[i]);
            }
        } catch (SQLException ex) {
            log.error("Error in prepareQueryStatementAndSetParameters()",ex);
        }
    }

}
