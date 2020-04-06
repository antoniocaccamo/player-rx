package me.antoniocaccamo.player.rx.timertask;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.ui.AbstractUI;

import java.util.TimerTask;

@Slf4j
public class ShowMediaTask extends TimerTask {
	
	
	private long start;		
	
	private long paused;
	
	private long duration;
	
	private long actual = 0;

	private AbstractUI abstractUI;
	
	
	public ShowMediaTask(AbstractUI abstractUI, long duration ) {
		this(abstractUI, 0, duration);
	}
	
	public ShowMediaTask(AbstractUI abstractUI, long paused, long duration) {
		this.start    = System.currentTimeMillis();
		this.duration = duration;
		this.paused   = paused;
		this.abstractUI = abstractUI;
		//player.getPlayerMaster().getScreenManager().resetVideoProgressBarMaximum();
		log.info("duration : {}", duration);
	}

	@Override
	public void run() {
		actual = System.currentTimeMillis() - start + paused;		
		if ( actual < duration ) {
			double aa = Double.valueOf(actual)    / 1000;
			double dd = Double.valueOf(duration)  / 1000; 
		    log.debug("screenManager.getIndex() [{}] updating video progress bar : [ {} / {} ]", abstractUI.getMonitorUI().getIndex() ,aa ,dd );
			abstractUI.updatePercentageProgess( aa, dd /*(int) x*/ );
		} else {			
			cancel();
			abstractUI.next();
		}
	}
	
//	public void setDuration( ) {
//		this.duration = duration;
//	}
	
	public long getActual() {
		return actual;
	}
	
}
