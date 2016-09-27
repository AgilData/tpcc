package com.codefutures.tpcc;

import com.codefutures.tpcc.load.FileLoader;
import com.codefutures.tpcc.load.JdbcPreparedStatementLoader;
import com.codefutures.tpcc.load.JdbcStatementLoader;
import com.codefutures.tpcc.load.RecordLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

/**
 * Copyright (C) 2011 CodeFutures Corporation. All rights reserved.
 */
public class TpccLoadConfig {

    enum LoadType {
        JDBC_STATEMENT,
        JDBC_PREPARED_STATEMENT,
        CSV
    }

    private LoadType loadType = LoadType.JDBC_PREPARED_STATEMENT;

    private Connection conn;

    private File outputDir;

    private boolean jdbcInsertIgnore = true;

    private int jdbcBatchSize = 1;

    public RecordLoader createLoader(String tableName, String columnName[]) throws IOException {
        switch (loadType) {
            case JDBC_STATEMENT:
                return new JdbcStatementLoader(conn, tableName, columnName, jdbcInsertIgnore, jdbcBatchSize);
            case JDBC_PREPARED_STATEMENT:
                return new JdbcPreparedStatementLoader(conn, tableName, columnName, jdbcInsertIgnore, jdbcBatchSize);
            case CSV:
                return new FileLoader(new File(outputDir, tableName + ".txt"));
            default:
                throw new IllegalStateException();
        }
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setLoadType(LoadType loadType) {
        this.loadType = loadType;
    }

    public LoadType getLoadType() {
        return loadType;
    }
}
