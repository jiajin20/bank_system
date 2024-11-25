package com.banksystem.application.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Database {
    private static Connection connection=null;
    public static Connection conn() {
        if (Objects.nonNull(connection)) {
            return connection;
        }
        String url="jdbc:mysql://localhost:3306/bank_system";
  try{
      //加载驱动
      Class.forName("com.mysql.jdbc.Driver");
      //建立连接
      connection=DriverManager.getConnection(url,"root", "123456");
  }catch (SQLException e){
      e.printStackTrace();
  }catch (ClassNotFoundException e){
      e.printStackTrace();
  }
  return connection;
    }
}
