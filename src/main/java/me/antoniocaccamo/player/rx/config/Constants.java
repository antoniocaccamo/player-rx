package me.antoniocaccamo.player.rx.config;


import java.text.MessageFormat;

public interface Constants {

        interface Player {

            String COMPUTER = "!!! must change me !!!";

            int WIDTH = 500;

            int HEIGHT = 400;

            int TOP    = 100;

            int LEFT   = 100;

            String DEFAULT_WEATHER_LATLNG = "45.08,7.40";

            String APP_COMPUTER = "app.computer";

            String APP_SEND_ALL_MAIL = "app.send.all.mail";

            String APP_ENABLED_LOG = "app.enabled.log";

            String APP_LOCALE = "app.locale";

            String APP_VERSION = "app.version";
            String APP_VERSION_DATE = "app.version.date";
            String APP_VERSION_INFO = "app.last.update.info";

            String APP_FTP_REFRESH = "app.ftp.refresh";

            String APP_SIZE_WIDTH = "app.size.width";

            String APP_SIZE_HEIGHT = "app.size.height";

            String APP_LOCATION_X = "app.location.x";

            String APP_LOCATION_Y = "app.location.y";

            String APP_MPLAYER_MODE = "app.mplayer.mode";
            String APP_MPLAYER_PATH = "app.mplayer.path";
            String APP_MPLAYER_OPTIONS = "app.mplayer.options";
            String APP_MPLAYER_WAIT_VIDEO_START = "app.mplayer.wait.video.start";


            String APP_TIME_LABEL_RATIO = "app.time.label.ratio";
            String APP_DATE_LABEL_RATIO = "app.date.label.ratio";

            String APP_PLAYER_VIDEO_WINDOWS_NUMBER = "app.player.video.windows.number";

            String APP_UPDATE_SITE = "app.update.site";

            String APP_PLAYER_WEATHER_LATLNG = "app.player.weather.latlng";
            String APP_PLAYER_WEATHER_REFRESH = "app.player.weather.refresh";


        }

        interface Screen {

            int WIDTH = 500;

            int HEIGHT = 400;

            int TOP    = 100;

            int LEFT   = 100;

            boolean ALL_DAY_DEFAULT = false;

            boolean LOCK_DEFAULT = false;

            boolean AUTOPLAY_DEFAULT = false;

            boolean VIEW_SCREEN_DEFAULT = true;


            int DEFAULT_ALPHA = 255;


            int DEFAULT_NUMBER_LOOP = 0;


            int DEFAULT_MONITOR = 1;


            MessageFormat APP_PLAYER_I_SIZE_WIDTH = new MessageFormat("app.player.{0}.size.width");

            MessageFormat APP_PLAYER_I_SIZE_HEIGHT = new MessageFormat("app.player.{0,number,#}.size.height");

            MessageFormat APP_PLAYER_I_LOCATION_X = new MessageFormat("app.player.{0,number,#}.location.x");

            MessageFormat APP_PLAYER_I_LOCATION_Y = new MessageFormat("app.player.{0,number,#}.location.y");

            MessageFormat APP_PLAYER_I_SEQUENCE_FILE = new MessageFormat("app.player.{0,number,#}.sequence.file");

            MessageFormat APP_PLAYER_I_SCREEN_LOCK = new MessageFormat("app.player.{0,number,#}.screen.lock");

            MessageFormat APP_PLAYER_I_SCREEN_FADE = new MessageFormat("app.player.{0,number,#}.screen.fade");

            MessageFormat APP_PLAYER_I_SCREEN_ALPHA = new MessageFormat("app.player.{0,number,#}.screen.alpha");

            MessageFormat APP_PLAYER_I_SCREEN_VIEW = new MessageFormat("app.player.{0,number,#}.screen.view");

            MessageFormat APP_PLAYER_I_SCREEN_FONT_TIME = new MessageFormat("app.player.{0,number,#}.screen.font.time");

            MessageFormat APP_PLAYER_I_SCREEN_FONT_DATE = new MessageFormat("app.player.{0,number,#}.screen.font.date");

            MessageFormat APP_PLAYER_I_ALL_DAY = new MessageFormat("app.player.{0,number,#}.activation.allDay");

            MessageFormat APP_PLAYER_I_TIMED = new MessageFormat("app.player.{0,number,#}.activation.timed");

            MessageFormat APP_PLAYER_I_TIMED_FROM = new MessageFormat("app.player.{0,number,#}.activation.timed.from");

            MessageFormat APP_PLAYER_I_TIMED_TO = new MessageFormat("app.player.{0,number,#}.activation.timed.to");

            MessageFormat APP_PLAYER_I_WHEN_NOT_ACTIVE_WATCH = new MessageFormat("app.player.{0,number,#}.not.active.watch");

            MessageFormat APP_PLAYER_I_WHEN_NOT_ACTIVE_BLACK = new MessageFormat("app.player.{0,number,#}.not.active.black");

            MessageFormat APP_PLAYER_I_WHEN_NOT_ACTIVE_IMAGE = new MessageFormat("app.player.{0,number,#}.not.active.image");

            MessageFormat APP_PLAYER_I_WATCH_IMAGE_FILE = new MessageFormat("app.player.{0,number,#}.watch.image.file");

            MessageFormat APP_PLAYER_I_SCREEN_COLOR_TIME = new MessageFormat("app.player.{0,number,#}.screen.color.time");

            MessageFormat APP_PLAYER_I_SCREEN_COLOR_DATE = new MessageFormat("app.player.{0,number,#}.screen.color.date");

            MessageFormat APP_PLAYER_I_SCREEN_MONITOR = new MessageFormat("app.player.{0,number,#}.screen.monitor");

            MessageFormat APP_PLAYER_I_WEATHER_LATLNG = new MessageFormat("app.player.{0,number,#}.weather.latlng");

            MessageFormat APP_PLAYER_I_LOOP_NUMBER = new MessageFormat("app.player.{0,number,#}.loop.number");

            String COLOR_SEPARATOR = "|";

        }


         enum TimingEnum {
            TIMED,
            ALL_DAY
        }

}
