package me.antoniocaccamo.player.rx.service;

import me.antoniocaccamo.player.rx.model.MainViewModel;

/**
 * @author antoniocaccamo on 07/02/2020
 */
public interface PreferenceService {

    MainViewModel read() ;

    void save();

}
