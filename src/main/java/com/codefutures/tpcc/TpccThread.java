package com.codefutures.tpcc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class TpccThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(TpccThread.class);
	private static final boolean DEBUG = logger.isDebugEnabled();

    /**
     * Dedicated JDBC connection for this thread.
     */
    Connection conn;

    Driver driver;
	
	int number;
	int port;
	int is_local;
	int num_ware;
	int num_conn;
	String db_user;
	String db_password;
	String driverClassName;
	String jdbcUrl;
    int fetchSize;

	private int[] success;
	private int[] late;
	private int[] retry;
	private int[] failure;

	private int[][] success2;
	private int[][] late2;
	private int[][] retry2;
	private int[][] failure2;

	//TpccStatements pStmts;
	
	public TpccThread(int number, int port, int is_local, String db_user, String db_password,
			int num_ware, int num_conn, String driverClassName, String dURL, int fetchSize,
			int[] success, int[] late, int[] retry, int[] failure, 
			int[][] success2, int[][] late2, int[][] retry2, int[][] failure2) {
		
        this.number = number;
        this.port = port;
        this.db_password = db_password;
        this.db_user = db_user;
        this.is_local = is_local;
        this.num_conn = num_conn;
        this.num_ware = num_ware;
        this.driverClassName = driverClassName;
        this.jdbcUrl = dURL;
        this.fetchSize = fetchSize;
        
		this.success = success;
		this.late = late;
		this.retry = retry;
		this.failure = failure;
		
		this.success2 = success2;
		this.late2 = late2;
		this.retry2 = retry2;
		this.failure2 = failure2;
		
        connectToDatabase();

        // Create a driver instance.
        driver = new Driver(conn, fetchSize,
                success, late, retry, failure,
                success2, late2, retry2, failure2);

    }

    public void run() {

        try {
            if (DEBUG) {
                logger.debug("Starting driver with: number: " + number + " num_ware: " + num_ware + " num_conn: " + num_conn);
            }

            driver.runTransaction(number, num_ware, num_conn);

        } catch (Throwable e) {
            logger.error("Unhandled exception", e);
        }

    }

    private Connection connectToDatabase() {

        logger.info("Connection to database: driver: " + driverClassName + " url: " + jdbcUrl);
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("Failed to load JDBC driver class: " + driverClassName, e1);
        }

        try {

            Properties prop = new Properties();
            File connPropFile = new File("conf/jdbc-connection.properties");
            if (connPropFile.exists()) {
                logger.info("Loading JDBC connection properties from " + connPropFile.getAbsolutePath());
                try {
                    final FileInputStream is = new FileInputStream(connPropFile);
                    prop.load(is);
                    is.close();

                    if (logger.isDebugEnabled()) {
                        logger.debug("Connection properties: {");
                        final Set<Map.Entry<Object,Object>> entries = prop.entrySet();
                        for (Map.Entry<Object,Object> entry : entries) {
                            logger.debug(entry.getKey() + " = " + entry.getValue());
                        }

                        logger.debug("}");
                    }

                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            else {
                logger.warn(connPropFile.getAbsolutePath() + " does not exist! Using default connection properties");
            }
            prop.put("user", db_user);
            prop.put("password", db_password);

            conn = DriverManager.getConnection(jdbcUrl, prop);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
        return conn;
    }
}

