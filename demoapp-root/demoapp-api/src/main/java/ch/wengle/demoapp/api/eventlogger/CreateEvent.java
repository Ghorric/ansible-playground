package ch.wengle.demoapp.api.eventlogger;

@FunctionalInterface
public interface CreateEvent {
	public void buildEvent(EventBuilder eventBuilder);
}
