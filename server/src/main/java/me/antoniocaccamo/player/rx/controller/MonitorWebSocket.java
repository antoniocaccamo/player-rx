package me.antoniocaccamo.player.rx.controller;

import java.util.function.Predicate;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerWebSocket("/monitor/{monitorId}")
public class MonitorWebSocket {

    private final WebSocketBroadcaster broadcaster;

    public MonitorWebSocket(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public void onOpen(String monitorId, WebSocketSession session) {
        String msg = String.format("{'status' : '%s joined'}", monitorId);
        broadcaster.broadcastSync(msg, isValid(session));
        log.info(msg);
        session.send(msg);
    }

    @OnMessage
    public void onMessage(String monitorId, String message, WebSocketSession session) {
        log.info(" monitor {}  received => {}", monitorId, message);
        String msg = String.format("{'[ECHO] monitor['%s] : received %s'}", monitorId, message);
        broadcaster.broadcastSync(msg, isValid(session));

    }

    private Predicate<WebSocketSession> isValid(WebSocketSession session) {
        return s -> s.equals(session);
    }
}
