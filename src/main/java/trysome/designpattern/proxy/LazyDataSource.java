package trysome.designpattern.proxy;

import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.sql.DataSource;
import java.sql.*;

public class LazyDataSource implements DataSource {
    private String url;

    private String username;

    private String password;

    public LazyDataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException{
        return new LazyConnectionProxy(()->{
            try {
            Connection conn = DriverManager.getConnection(url,username, password);
            System.out.println("open connection: " + conn);
            return conn;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        });

    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(this.username, this.password);
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
