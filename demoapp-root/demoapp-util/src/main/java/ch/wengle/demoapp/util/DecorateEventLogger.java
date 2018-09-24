package ch.wengle.demoapp.util;

import ch.wengle.demoapp.api.EventLogger;

public class DecorateEventLogger implements EventLogger {
	private EventLogger eventLogger;
	private String decorate;

	@Override
	public EventLogger info(String txt, Object... params) {
		eventLogger.info(txt + " - "+ decorate, params);
		return this;
	}

	public void setEventLogger(EventLogger eventLogger) {
		this.eventLogger = eventLogger;
	}

	public void setDecorate(String decorate) {
		this.decorate = decorate;
	}
	
}
