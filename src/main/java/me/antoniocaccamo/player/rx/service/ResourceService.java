package me.antoniocaccamo.player.rx.service;

import me.antoniocaccamo.player.rx.model.resource.Resource;

import java.util.Map;

public interface ResourceService {

    Map<String, Resource> getResourceMap();

    Iterable<Resource> getResources();
}
