package trysome.designpattern.proxy;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource {
    private String url;
    private String username;
    private String password;

    private Queue<PooledConnectionProxy> idleQueue = new ArrayBlockingQueue<>(100);

    public PooledDataSource(String url, String user, String password) {
        this.url = url;
        this.username = user;
        this.password = password;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException{
        PooledConnectionProxy conn = idleQueue.poll();
        if (conn == null){
            conn = openNewConnection();
        }else {
            System.out.println("Return pooled connection: "+ conn.target);
        }
        return conn;
    }

    private PooledConnectionProxy openNewConnection() throws SQLException{
        Connection conn = DriverManager.getConnection(url,username,password);
        System.out.println("Open new connection: " + conn);
        return new PooledConnectionProxy(idleQueue,conn);
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
