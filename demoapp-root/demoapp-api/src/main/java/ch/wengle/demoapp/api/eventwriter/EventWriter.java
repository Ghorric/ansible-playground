package ch.wengle.demoapp.api.eventwriter;

import ch.wengle.demoapp.api.event.Event;

@FunctionalInterface
public interface EventWriter {
	public void writeEvent(Event event);
}
