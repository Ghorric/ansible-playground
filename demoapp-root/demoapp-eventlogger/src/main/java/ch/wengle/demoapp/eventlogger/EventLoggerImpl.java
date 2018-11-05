package ch.wengle.demoapp.eventlogger;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.event.Event;
import ch.wengle.demoapp.api.event.EventKey;
import ch.wengle.demoapp.api.eventlogger.CreateEvent;
import ch.wengle.demoapp.api.eventlogger.EventLogger;
import ch.wengle.demoapp.api.eventlogger.Severity;
import ch.wengle.demoapp.api.eventwriter.EventWriter;
import ch.wengle.demoapp.api.msg.Header;

public class EventLoggerImpl implements EventLogger {
	final Logger log = LoggerFactory.getLogger(EventLoggerImpl.class);

	private final LocalFact localFact;
	private List<EventWriter> eventWriters;

	public EventLoggerImpl() {
		this(new LocalFact());
	}

	protected EventLoggerImpl(LocalFact localFact) {
		this.localFact = localFact;
	}

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
		Objects.requireNonNull(severity); // TODO: Implement Severity
		EventBuilderImpl builder = localFact.eventBuilder();
		Objects.requireNonNull(createEvent).buildEvent(builder);
		Map<EventKey, String> data = builder.getData();
		enrich(data, severity);
		Event event = createEvent(data);
		eventWriters.forEach(ew -> ew.writeEvent(event));
		return this;
	}

	protected void enrich(Map<EventKey, String> data, Severity severity) {
		data.put(Header.SEVERITY, severity.name());
	}

	protected Event createEvent(Map<EventKey, String> data) {
		return new Event(data);
	}

	public void setEventWriters(List<EventWriter> eventWriters) {
		this.eventWriters = eventWriters;
	}

	protected static class LocalFact {
		public EventBuilderImpl eventBuilder() {
			return new EventBuilderImpl();
		}
	}

}
