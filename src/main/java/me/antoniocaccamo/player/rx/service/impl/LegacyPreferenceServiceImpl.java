
package me.antoniocaccamo.player.rx.service.impl;

import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants.Player;
import me.antoniocaccamo.player.rx.config.Constants.Screen;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.model.preference.LocationModel;
import me.antoniocaccamo.player.rx.model.preference.MonitorModel;
import me.antoniocaccamo.player.rx.model.preference.PreferenceModel;
import me.antoniocaccamo.player.rx.model.preference.SizeModel;
import me.antoniocaccamo.player.rx.service.LegacyPreferenceService;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author antoniocaccamo on 07/04/2020
 */
@Singleton @Slf4j
public class LegacyPreferenceServiceImpl implements LegacyPreferenceService {

    @Value("${micronaut.application.legacy-pref-file}") @NotNull
    private String prefFile;

    @Override
    public PreferenceModel load() throws IOException {

        PreferenceModel preferenceModel = null ;

       
            Properties props = new Properties();
            props.load( new FileReader(prefFile));

            preferenceModel = new PreferenceModel();

            String cn = props.getProperty(Constants.Player.APP_COMPUTER);
            if ( StringUtils.isNotEmpty(cn) ) {
               preferenceModel.setComputer( cn );
            } else {
                try {
                    preferenceModel.setComputer(InetAddress.getLocalHost().getHostName());
                } catch (Exception e ) {
                    e.printStackTrace(System.err);
                    preferenceModel.setComputer (System.getenv("COMPUTERNAME"));
                }
            }
/*
            Player.APP_SEND_ALL_MAIL = preferenceStore.getBoolean(Constants.Player.APP_SEND_ALL_MAIL);

            boolean enabledLog = preferenceStore.getBoolean(Constants.Player.APP_ENABLED_LOG);

            enableLog(enabledLog);

            log.info("AT ADV Player ");
            log.info("version @ " + Player.VERSION + " " + VERSION_DATE);

            String locale = preferenceStore.getString(Constants.Player.APP_LOCALE);
            if (StringUtils.isEmpty(locale)) {
                LocaleManager.changeLocale(Locale.getDefault().toString());
            } else {
                LocaleManager.changeLocale(locale);
            }

            String updateSite = preferenceStore.getString(Constants.Player.APP_UPDATE_SITE);
            if (!Utils.isAnEmptyString(updateSite)) {
                Player.UPDATE_SITE = updateSite;
            } else {
                Player.UPDATE_SITE = "http://www.arttechonline.com/atadvplayer/jupdate/update.php";
            }

            double ftpRefresh = preferenceStore.getDouble(Constants.Player.APP_FTP_REFRESH);
            if (ftpRefresh == 0) {
                ftpRefresh = Constants.Player.FTP_REFRESH_DEFAULT;
            }
            Player.APP_FTP_REFRESH = ftpRefresh;

            String versionInfo = preferenceStore.getString(Constants.Player.APP_VERSION_INFO);
            if (!Utils.isAnEmptyString(versionInfo)) {
                Player.VERSION_INFO = versionInfo;
            }

            String mplayer = preferenceStore.getString(Constants.Player.APP_MPLAYER_PATH);

            if (!Utils.isAnEmptyString(mplayer)) {
                //				File file = new File(  System.getProperty("user.dir") + "/" + mplayer);
                File file = new File(mplayer);
                if (file.exists() && file.isFile() && file.canExecute()) {
                    Player.MPLAYER_PATH = mplayer;
                }
            }

            String options = preferenceStore.getString(Constants.Player.APP_MPLAYER_OPTIONS);
            if (!Utils.isAnEmptyString(options)) {
                Player.MPLAYER_OPTIONS = options;
            }

            String mplayerMode = StringUtils.isNotEmpty(preferenceStore.getString(Constants.Player.APP_MPLAYER_MODE)) ?
                    preferenceStore.getString(Constants.Player.APP_MPLAYER_MODE) : MPlayerMode.IDLE.name();
            for ( MPlayerMode mm : MPlayerMode.values() ) {
                if ( StringUtils.endsWithIgnoreCase(mm.name(), mplayerMode))
                    Player.MPLAYER_MODE =  mm;
            };

            log.info("Player.MPLAYER_MODE = {}" , Player.MPLAYER_MODE);

            Player.APP_MPLAYER_WAIT_VIDEO_START = preferenceStore.getInt(Constants.Player.APP_MPLAYER_WAIT_VIDEO_START);
            if ( Player.APP_MPLAYER_WAIT_VIDEO_START == 0 ){
                Player.APP_MPLAYER_WAIT_VIDEO_START = 2;
            }

            Player.APP_TIME_LABEL_RATIO = preferenceStore.getInt(Constants.Player.APP_TIME_LABEL_RATIO);
            Player.APP_DATE_LABEL_RATIO = preferenceStore.getInt(Constants.Player.APP_DATE_LABEL_RATIO);
*/
            int width  = Integer.valueOf( props.getProperty(Constants.Player.APP_SIZE_WIDTH, String.valueOf(Constants.Player.WIDTH)));
            int heigth =  Integer.valueOf(props.getProperty(Constants.Player.APP_SIZE_HEIGHT, String.valueOf(Constants.Player.HEIGHT)));
            
            preferenceModel.setSize( new SizeModel(width, heigth));
            
            

            int x =  Integer.valueOf( props.getProperty(Constants.Player.APP_LOCATION_X, String.valueOf(Constants.Player.TOP)));
            int y =  Integer.valueOf( props.getProperty(Constants.Player.APP_LOCATION_Y, String.valueOf(Constants.Player.LEFT)));

            preferenceModel.setLocation( new LocationModel(x, y));

            preferenceModel.setWeatherLatlng( props.getProperty(Constants.Player.APP_PLAYER_WEATHER_LATLNG, Constants.Player.DEFAULT_WEATHER_LATLNG));
/*
            weatherLatlng = preferenceStore.getString(Constants.Player.APP_PLAYER_WEATHER_LATLNG);
            if ( StringUtils.isEmpty(weatherLatlng)){
                weatherLatlng = " 45.08,7.40";
            }

            // minutes
            weatherRefresh = preferenceStore.getInt(Constants.Player.APP_PLAYER_WEATHER_REFRESH);
            if ( weatherRefresh == 0 ) {
                weatherRefresh = 180;
            }
*/
        List<MonitorModel> monitorModels = new ArrayList<>();
        int numOfWindows = Integer.valueOf( props.getProperty(Constants.Player.APP_PLAYER_VIDEO_WINDOWS_NUMBER, String.valueOf(1)));

            if (numOfWindows > 0) {

                for (int i = 1; i <= numOfWindows; i++) {
                    MonitorModel monitorModel = new MonitorModel();
                    Integer[] idx = new Integer[1];
                    idx[0] = new Integer(i);
     

                    int pw = Integer.valueOf( props.getProperty(Constants.Screen.APP_PLAYER_I_SIZE_WIDTH.format(idx),  String.valueOf(Constants.Screen.WIDTH)));
                    int ph = Integer.valueOf( props.getProperty(Constants.Screen.APP_PLAYER_I_SIZE_HEIGHT.format(idx), String.valueOf(Constants.Screen.HEIGHT)));
                    monitorModel.setSize( new SizeModel(pw, ph));
                    
                    int pt = Integer.valueOf( props.getProperty(Constants.Screen.APP_PLAYER_I_LOCATION_X.format(idx),String.valueOf(Constants.Screen.TOP) ) );
                    int pl = Integer.valueOf( props.getProperty(Constants.Screen.APP_PLAYER_I_LOCATION_Y.format(idx),String.valueOf(Constants.Screen.LEFT) ) );
                    
                    monitorModel.setLocation(new LocationModel(pt, pl));



                    String s = props.getProperty(Constants.Screen.APP_PLAYER_I_SEQUENCE_FILE.format(idx));
                    monitorModel.setSequence(s);
/*
                    int loop = preferenceStore.getInt(Constants.Screen.APP_PLAYER_I_LOOP_NUMBER.format(idx));
                    monitorModel.setLoopNumber(loop);

                    boolean lock = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_SCREEN_LOCK.format(idx));
                    monitorModel.setLock(lock);

                    boolean fade = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_SCREEN_FADE.format(idx));
                    monitorModel.setFade(fade);

                    int alpha = preferenceStore.getInt(Constants.Screen.APP_PLAYER_I_SCREEN_ALPHA.format(idx));
                    monitorModel.setAlpha(alpha);

                    boolean view = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_SCREEN_VIEW.format(idx));
                    monitorModel.setViewScreen(view);

                    */

                    log.warn(" to continue ...");

//                    boolean allDay = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_ALL_DAY.format(idx));
//                    monitorModel.setAllDay(allDay);
//
//                    boolean timed = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_TIMED.format(idx));
//                    monitorModel.setTimed(timed);

                    monitorModel.setTiming(Constants.TimingEnum.ALL_DAY);
//
//                    if (timed) {
//                        String from = preferenceStore.getString(Constants.Screen.APP_PLAYER_I_TIMED_FROM.format(idx));
//                        String to = preferenceStore.getString(Constants.Screen.APP_PLAYER_I_TIMED_TO.format(idx));
//                        LocalTime df = null;
//                        LocalTime dt = null;
//                        try {
//                            df = ISODateTimeFormat.hourMinute().parseLocalTime(from);
//
//                            dt = ISODateTimeFormat.hourMinute().parseLocalTime(to);
//
//                        } catch (Exception e) {
//                            df = ISODateTimeFormat.hourMinuteSecond().parseLocalTime(from);
//                            dt = ISODateTimeFormat.hourMinuteSecond().parseLocalTime(to);
//                        }
//
//                        if (df != null && dt != null) {
//                            df = df.withSecondOfMinute(0);
//                            dt = dt.withSecondOfMinute(0);
//                            log.debug("timed : from [{}] to [{}]", df, dt);
//                            monitorModel.setFrom(df);
//                            monitorModel.setTo(dt);
//                        }
//                    }
//
//                    boolean black = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_WHEN_NOT_ACTIVE_BLACK.format(idx));
//                    monitorModel.setWhenNotActiveBlack(black);
//
//                    boolean watch = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_WHEN_NOT_ACTIVE_WATCH.format(idx));
//                    monitorModel.setWhenNotActiveWatch(watch);
//
//                    boolean img = preferenceStore.getBoolean(Constants.Screen.APP_PLAYER_I_WHEN_NOT_ACTIVE_IMAGE.format(idx));
//                    monitorModel.setWhenNotActiveImage(img);
//
//                    String timeFontDataString
//                            = preferenceStore.getString(Constants.Screen.APP_PLAYER_I_SCREEN_FONT_TIME.format(idx));
//                    if (!Utils.isAnEmptyString(timeFontDataString)) {
//                        try {
//                            FontData timeFontData = new FontData(timeFontDataString);
//                            monitorModel.setTimeLabelFontData(timeFontData);
//                        } catch (Exception e) {
//                            log.error("time font date error", e);
//                        }
//                    }
//
//                    String dateFontDataString
//                            = preferenceStore.getString(Constants.Screen.APP_PLAYER_I_SCREEN_FONT_DATE.format(idx));
//                    if (!Utils.isAnEmptyString(dateFontDataString)) {
//                        try {
//                            FontData dateFontData = new FontData(dateFontDataString);
//                            monitorModel.setDateLabelFontData(dateFontData);
//                        } catch (Exception e) {
//                            log.error("date font date error", e);
//                        }
//                    }
//
//                    String watchImageString
//                            = preferenceStore.getString(Constants.Screen.APP_PLAYER_I_WATCH_IMAGE_FILE.format(idx));
//                    if (!Utils.isAnEmptyString(watchImageString)) {
//                        File watchImageFile = new File(watchImageString);
//                        if (watchImageFile.exists() && watchImageFile.isFile() && watchImageFile.canRead()) {
//                            monitorModel.setWatchImageFile(watchImageFile);
//                        }
//                    }
//
//                    String timeColor
//                            = preferenceStore.getString(Constants.Screen.APP_PLAYER_I_SCREEN_COLOR_TIME.format(idx));
//                    if (!Utils.isAnEmptyString(timeColor)) {
//                        StringTokenizer st = new StringTokenizer(timeColor, Constants.Screen.COLOR_SEPARATOR);
//                        if (st.countTokens() == 3) {
//                            try {
//                                String rs = st.nextToken();
//                                String gs = st.nextToken();
//                                String bs = st.nextToken();
//
//                                int red = Integer.parseInt(rs);
//                                int green = Integer.parseInt(gs);
//                                int blue = Integer.parseInt(bs);
//
//                                RGB rgb = new RGB(red, green, blue);
//                                monitorModel.setTimeLabelFontColor(rgb);
//                            } catch (NumberFormatException e) {
//                                log.error("time color error", e);
//                            }
//                        }
//                    }
//
//                    String dateColor
//                            = preferenceStore.getString(Constants.Screen.APP_PLAYER_I_SCREEN_COLOR_DATE.format(idx));
//                    if (!Utils.isAnEmptyString(dateColor)) {
//                        StringTokenizer st = new StringTokenizer(dateColor, Constants.Screen.COLOR_SEPARATOR);
//                        if (st.countTokens() == 3) {
//                            try {
//                                String rs = st.nextToken();
//                                String gs = st.nextToken();
//                                String bs = st.nextToken();
//
//                                int red = Integer.parseInt(rs);
//                                int green = Integer.parseInt(gs);
//                                int blue = Integer.parseInt(bs);
//
//                                RGB rgb = new RGB(red, green, blue);
//                                monitorModel.setDateLabelFontColor(rgb);
//                            } catch (NumberFormatException e) {
//                                log.error("time color error", e);
//                            }
//                        }
//                    }
//
//                    int monitor = preferenceStore.getInt(Constants.Screen.APP_PLAYER_I_SCREEN_MONITOR.format(idx));
//                    monitorModel.setMonitor(monitor == 0 ? ScreenSetting.DEFAULT_MONITOR: monitor);
//
//                    this.playerSetting.add(monitorModel);
                    monitorModels.add(monitorModel);
                }
            } else {
                MonitorModel monitorModelDefault = new MonitorModel();
                
                monitorModels.add(monitorModelDefault);
            }

        preferenceModel.setMonitors(monitorModels);

        

        return preferenceModel;


    }


}
