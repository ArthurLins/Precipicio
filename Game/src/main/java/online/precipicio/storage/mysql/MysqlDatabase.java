package online.precipicio.storage.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class MysqlDatabase {
    private static MysqlDatabase ourInstance = new MysqlDatabase();

    public static MysqlDatabase getInstance() {
        return ourInstance;
    }

    public static void initialize(){
        if (ourInstance == null){
            ourInstance = new MysqlDatabase();
        }
    }

    private HikariDataSource dataSource;

    private MysqlDatabase() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/simpsons");
        config.setUsername("bart");
        config.setPassword("51mp50n");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.dataSource = new HikariDataSource(config);
    }


    public ResultSet query(PreparedStatement statement){
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                statement.getConnection().close();
                statement.close();
                if (resultSet != null){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return resultSet;
    }



    public ResultSet query(String query, Object... values){
        Connection  connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            int index = 1;
            for (Object value : values){
                if (value instanceof String){
                    statement.setString(index, (String) value);
                } else if(value instanceof Double){
                    statement.setDouble(index, (double) value);
                } else if (value instanceof Integer){
                    statement.setInt(index, (int) value);
                } else if (value instanceof Float) {
                    statement.setFloat(index, (float) value);
                } else if (value instanceof Short){
                    statement.setShort(index, (Short) value);
                } else if (value instanceof Time){
                    statement.setTime(index, (Time) value);
                } else if (value instanceof Timestamp){
                    statement.setTimestamp(index, (Timestamp) value);
                } else if (value == null){
                    statement.setNull(index,0);
                }
                index++;
            }
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();

            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return resultSet;
    }

    public Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
