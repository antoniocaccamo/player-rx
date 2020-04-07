package me.antoniocaccamo.player.rx.service;

import me.antoniocaccamo.player.rx.model.preference.PreferenceModel;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author antoniocaccamo on 07/04/2020
 */
public interface LegacyPreferenceService {

    PreferenceModel load() throws IOException;
}
