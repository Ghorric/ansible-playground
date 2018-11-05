package ch.wengle.demoapp.util;

import static ch.wengle.demoapp.api.msg.Header.EVENT_TXT;
import static ch.wengle.demoapp.api.msg.Header.MSG_BODY;
import static ch.wengle.demoapp.api.msg.Header.MSG_ID;
import static ch.wengle.demoapp.api.msg.Header.SEVERITY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.event.Event;
import ch.wengle.demoapp.api.event.EventKey;
import ch.wengle.demoapp.api.eventlogger.Severity;
import ch.wengle.demoapp.api.eventwriter.EventWriter;

public class EventWriterLog implements EventWriter {
	final Logger log = LoggerFactory.getLogger(EventWriterLog.class);

	final Set<EventKey> NO_KEY = new HashSet<>(Arrays.asList(SEVERITY, MSG_ID, EVENT_TXT));

	@Override
	public void writeEvent(Event event) {
		Severity severity = Severity.valueOf(event.get(SEVERITY));
		List<EventKey> order = orderKeys(Objects.requireNonNull(severity), event.getKeys());
		String logMsg = createLogMsg(event, order);
		writeLog(severity, logMsg);
	}
	
	protected void writeLog(Severity severity, String logMsg) {
		switch (Objects.requireNonNull(severity)) {
		case DEBUG:
			if (log.isDebugEnabled())
				log.debug(logMsg);
			break;
		case INFO:
			if (log.isInfoEnabled())
				log.info(logMsg);
			break;
		case WARN:
			if (log.isWarnEnabled())
				log.warn(logMsg);
			break;
		case ERROR:
			if (log.isErrorEnabled())
				log.error(logMsg);
			break;
		default:
			throw new IllegalStateException("Unexpected: " + severity);
		}
	}

	protected String createLogMsg(Event event, List<EventKey> order) {
		Stream<StringBuffer> stream = order.stream().map(key -> NO_KEY.contains(key) ? new StringBuffer(event.get(key))  : new StringBuffer(key.name() + "=" + event.get(key)));
		StringBuffer sb = stream.reduce(new StringBuffer(), (r, n) -> r.append(" | ").append(n));
		return sb.toString();
	}

	protected List<EventKey> orderKeys(Severity severity, Set<EventKey> all) {
		List<EventKey> list = keys(SEVERITY, MSG_ID, EVENT_TXT);
		List<EventKey> remaining = new ArrayList<>(all);
		list.forEach(e -> remaining.remove(e));
		list.addAll(remaining);
		if (Severity.ERROR != severity)
			list.remove(MSG_BODY);
		return list;
	}

	protected List<EventKey> keys(EventKey... keys) {
		return new ArrayList<>(Arrays.asList(keys));
	}

}
