package ro.ubbcluj.cs.thehound;

import ro.ubbcluj.cs.thehound.net.ServerRpc;
import ro.ubbcluj.cs.thehound.repository.Repository;
import ro.ubbcluj.cs.thehound.service.ServerManager;
import ro.ubbcluj.cs.thehound.utils.Constants;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cristi on 11/13/2016.
 */

public class ServerMain {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Repository repository = new Repository(Constants.JDBC_DIRVER, Constants.SQL_DATABASE);
        ServerRpc serverRpc = new ServerRpc(executorService);

        new ServerManager(repository, serverRpc);

        serverRpc.start();
    }
}
