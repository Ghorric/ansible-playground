package ch.wengle.demoapp.api;

public interface EventLogger {
	public EventLogger info(String txt, Object... params);
}
