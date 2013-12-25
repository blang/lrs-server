package de.benediktlang.lrsserver;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HTTPRequestHandler extends AbstractHandler {
    private static final Logger LOG = Logger.getLogger(HTTPRequestHandler.class);
    private MessageStore msgStore;

    public HTTPRequestHandler(MessageStore msgStore) {
        this.msgStore = msgStore;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("Request: " + s);
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (s.equalsIgnoreCase("/msgs")) {
            response.setContentType("text/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            request.setHandled(true);
            response.getWriter().println(msgStore.toJson());
        } else {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setHandled(true);
            response.getWriter().println("");
        }
    }
}
