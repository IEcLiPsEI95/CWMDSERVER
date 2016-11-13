package ro.ubbcluj.cs.thehound.service;

import ro.ubbcluj.cs.thehound.net.ServerRpc;
import ro.ubbcluj.cs.thehound.repository.Repository;

/**
 * Created by cristi on 11/13/2016.
 */
public class ServerManager {
    private Repository repository;
    private ServerRpc serverRpc;

    public ServerManager(Repository repository, ServerRpc serverRpc) {
        this.repository = repository;
        this.serverRpc = serverRpc;
    }
}
