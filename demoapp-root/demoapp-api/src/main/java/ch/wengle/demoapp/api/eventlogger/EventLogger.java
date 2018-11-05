package ch.wengle.demoapp.api.eventlogger;

public interface EventLogger {
	public EventLogger debug(CreateEvent createEvent);
	public EventLogger info(CreateEvent createEvent);
	public EventLogger warn(CreateEvent createEvent);
	public EventLogger error(CreateEvent createEvent);
	public EventLogger event(Severity severity, CreateEvent createEvent);
}
