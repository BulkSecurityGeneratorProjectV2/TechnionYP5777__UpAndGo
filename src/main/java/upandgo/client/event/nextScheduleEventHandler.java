package upandgo.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Omri Ben- Shmuel
 * @since 19-04-17
 * 
 * Interface for handling the {@link clearScheduleEvent}.
 * 
 */

public interface nextScheduleEventHandler extends EventHandler {
	void onNextSchedule(nextScheduleEvent e);
}

