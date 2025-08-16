package dnt.websockets.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.server.infrastructure.AbstractRequest;
import dnt.websockets.server.infrastructure.MessagePublisher;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dnt.websockets.VertxFactory.newVertx;

public class Server
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_CONNECT = 100;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
    private static final ObjectReader MESSAGE_READER = OBJECT_MAPPER.readerFor(AbstractRequest.class);
    private Vertx vertx;
    private HttpServer httpServer;

    public static void main(String[] args)
    {
        new Server().go();
    }

    public Future<HttpServer> go()
    {
        vertx = newVertx();
        httpServer = vertx.createHttpServer();
        return httpServer
                .webSocketHandler(this::handle)
                .listen(7777);
    }

    private void handle(ServerWebSocket serverWebSocket)
    {
        if(!"v1/websocket".equals(serverWebSocket.path()))
        {
            LOGGER.warn("Failed to connect websocket");
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_CONNECT);
            return;
        }

        MessagePublisher messagePublisher = new MessagePublisher(serverWebSocket, OBJECT_MAPPER);
        WebsocketTextMessageHandler textMessageHandler = new WebsocketTextMessageHandler(MESSAGE_READER, messagePublisher);
        serverWebSocket.textMessageHandler(textMessageHandler);
    }

    public void close()
    {
        httpServer.close();
        vertx.close();
    }
}
