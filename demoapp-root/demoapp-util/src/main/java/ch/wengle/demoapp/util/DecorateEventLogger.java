package ch.wengle.demoapp.util;

import ch.wengle.demoapp.api.eventlogger.CreateEvent;
import ch.wengle.demoapp.api.eventlogger.EventLogger;
import ch.wengle.demoapp.api.eventlogger.Severity;

public class DecorateEventLogger implements EventLogger {
	private EventLogger eventLogger;
	private String decorate;

	@Override
	public EventLogger debug(CreateEvent createEvent) {
		return event(Severity.DEBUG, createEvent);
	}

	@Override
	public EventLogger info(CreateEvent createEvent) {
		return event(Severity.INFO, createEvent);
	}

	@Override
	public EventLogger warn(CreateEvent createEvent) {
		return event(Severity.WARN, createEvent);
	}

	@Override
	public EventLogger error(CreateEvent createEvent) {
		return event(Severity.ERROR, createEvent);
	}

	@Override
	public EventLogger event(Severity severity, CreateEvent createEvent) {
		return eventLogger.event(severity, b -> {
			createEvent.buildEvent(b);
			b.property(() -> "DECORATE", decorate);
		});
	}

	public void setEventLogger(EventLogger eventLogger) {
		this.eventLogger = eventLogger;
	}

	public void setDecorate(String decorate) {
		this.decorate = decorate;
	}

}
