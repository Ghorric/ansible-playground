package ch.wengle.demoapp.util;

import java.util.function.BiConsumer;
import java.util.logging.Logger;

import ch.wengle.demoapp.api.EventLogger;

public class OsgiServiceCallerEventLogger extends OsgiServiceCaller<EventLogger> {
	public static Logger logger = Logger.getLogger(OsgiServiceCallerEventLogger.class.getName());

	@Override
	protected Class<EventLogger> getType() {
		return EventLogger.class;
	}

	@Override
	protected BiConsumer<EventLogger, Object> getServiceConsumer() {
		return (service, body) -> service.info((String) body, "param");
	}

}
