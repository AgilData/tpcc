package com.codefutures.tpcc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by andy on 9/22/16.
 */
public class TpccTest {

  private static final Logger logger = LoggerFactory.getLogger(TpccTest.class);

  static Connection conn;

  public static void main(String[] args) throws Exception {

    conn = connectToDatabase();

//    PreparedStatement pstmt = prepareStatement("SELECT c_id, c_first FROM customer WHERE c_id = 1");
//    ResultSet rs = pstmt.executeQuery();
//    while (rs.next()) {
//      System.out.println(rs.getInt(1) + "\t" + rs.getString(2));
//    }
//    rs.close();

//    PreparedStatement pstmt = prepareStatement("INSERT INTO new_orders (no_o_id, no_d_id, no_w_id) VALUES (?,?,?)");
//    pstmt.setInt(1, 1);
//    pstmt.setInt(2, 1);
//    pstmt.setInt(3, 1);
//    pstmt.execute();

    Statement stmt = conn.createStatement();
    stmt.execute("INSERT INTO new_orders (no_o_id, no_d_id, no_w_id) VALUES (-1,-2,-3), (4, 5, 6)");
  }

  private static PreparedStatement prepareStatement(String sql) throws SQLException {
    if (sql.startsWith("SELECT")) {
      return conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    } else {
      return conn.prepareStatement(sql, PreparedStatement.NO_GENERATED_KEYS);
    }
  }

  private static Connection connectToDatabase() throws Exception {
    String driverClassName = "com.mysql.jdbc.Driver";
    String jdbcUrl = "jdbc:mysql://127.0.0.1:3307/tpcc?useSSL=false&serverTimezone=UTC&autoReconnect=true&useServerPrepStmts=true";

    logger.info("Connection to database: driver: " + driverClassName + " url: " + jdbcUrl);
    try {
      Class.forName(driverClassName);
    } catch (ClassNotFoundException e1) {
      throw new RuntimeException("Failed to load JDBC driver class: " + driverClassName, e1);
    }

    Properties prop = new Properties();
      prop.put("user", "agiluser");
      prop.put("password", "password123");

      conn = DriverManager.getConnection(jdbcUrl, prop);
      conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
      conn.setAutoCommit(false);


    return conn;
  }


}
