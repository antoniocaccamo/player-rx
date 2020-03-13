package me.antoniocaccamo.player.rx.service;

import me.antoniocaccamo.player.rx.model.resource.Resource;

public interface ResourceService {

    Iterable<Resource> getResourceMap();
}
