package ch.wengle.demoapp.util;

import java.util.function.BiConsumer;

import ch.wengle.demoapp.api.eventlogger.EventLogger;

public class OsgiServiceCallerEventLogger extends OsgiServiceCaller<EventLogger> {

	@Override
	protected Class<EventLogger> getType() {
		return EventLogger.class;
	}

	@Override
	protected BiConsumer<EventLogger, Object> getServiceConsumer() {
		return (service, body) -> service.info(b -> b.txt((String) body));
	}

}
