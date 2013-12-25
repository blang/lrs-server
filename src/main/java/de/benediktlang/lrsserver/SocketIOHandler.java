package de.benediktlang.lrsserver;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import de.benediktlang.lrsserver.entity.LogMsg;
import org.apache.log4j.Logger;

/**
 * Created by ben on 12/24/13.
 */
public class SocketIOHandler {
    private static final Logger LOG = Logger.getLogger(SocketIOHandler.class);

    private SocketIOServer server;
    private int port;

    public SocketIOHandler(int port) {
        this.port = port;
    }

    public void bootServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(port);

        server = new SocketIOServer(config);
        server.start();
    }

    public void broadcast(LogMsg msg) {
        try {
            LOG.debug("Log Event via socket.io: " + msg);
            server.getBroadcastOperations().sendEvent("logevent", msg.toJson());
        } catch (Exception e) {
            LOG.debug("Exception while broadcasting over socket.io", e);
        }
    }
}
