package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.Layouts;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.timertask.ShowMediaTask;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import java.time.LocalDateTime;
import java.util.Timer;

/**
 * @author antoniocaccamo on 19/02/2020
 */
@Slf4j
public abstract class AbstractUI extends Composite {

    private final MonitorUI monitorUI;
    private ShowMediaTask durationTask;

    public AbstractUI(MonitorUI monitorUI, Composite parent) {
        super(parent, SWT.NONE);
        Layouts.setGrid(this)
                .margin(0)
                .numColumns(1)
                .columnsEqualWidth(true)
        ;
        Layouts.setGridData(this)
                .grabAll()
        ;

        this.monitorUI = monitorUI;
    }

    protected long startInMillis;

    protected long pausedAt;

    protected Timer durationTimer = new Timer();
    protected Timer alphaTimer    = new Timer();

    protected Media current;

    protected boolean alphaEnabled;

    protected Composite composite = null;

    public void play()  {
        if ( ! current.isAvailable() ) {
            next();
        }
        log.debug("playyyyyyyy");
        this.startInMillis = LocalDateTime.now().getNano();
        durationTask = new ShowMediaTask(this, Float.valueOf(current.getDuration().toMillis()).intValue() );
        durationTimer.schedule( durationTask, 0, 100 );
//		if ( playerMaster.getScreenManager().getPlayerSetting().isFade()  ) {
//			logger.debug("init alpha timer task");
//			alphaTask = new IPlayerAlphaTimerTask(this);
//			alphaTimer.schedule( alphaTask , 0 , 30);
//			alphaEnabled = true;
//		}
    }

    public void next() {
        durationTimer.purge();
        monitorUI.next();
    }

    public void stop() {
        if ( durationTask != null )
            this.durationTask.cancel();
        durationTimer.purge();
    }

    public Media getMedia(){
        return current;
    }

    public void setMedia(Media movie) {
        this.current = movie ;
    }

    public void pause() {
        this.pausedAt = durationTask.getActual();
        durationTask.cancel();
        durationTimer.purge();
    }

    public void resume() {
        long duration = Float.valueOf( current.getDuration().toMillis() ).intValue();
        durationTask = new ShowMediaTask(this, this.pausedAt, duration);
        durationTimer.schedule(durationTask, 0, 100);
    }

    public void errorOnPlay(Throwable throwable) {
        monitorUI.errorOnPlay(throwable);
    }

    public  void updatePercentageProgess(double aa, double dd) {
         double percentage =  aa / dd * 100 ;
        log.info("updatePercentageProgesse : {}", percentage);
        monitorUI.updatePercentageProgess( (int) percentage  );
    }

    public MonitorUI getMonitorUI() {
        return monitorUI;
    }
}
