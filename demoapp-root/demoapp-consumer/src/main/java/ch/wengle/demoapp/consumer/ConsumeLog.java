package ch.wengle.demoapp.consumer;

import java.util.Map;
import java.util.logging.Logger;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import ch.wengle.demoapp.api.EventLogger;

@Component
public class ConsumeLog {
	public static Logger logger = Logger.getLogger(ConsumeLog.class.getName());

	EventLogger eventLogger;

	@Activate
	void activate(Map<String, Object> properties) {
		logger.fine("ConsumeLog.activate(): " + properties);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
	public void bindWriteLog(EventLogger eventLogger) {
		this.eventLogger = eventLogger;
		logger.fine("ConsumeLog.gotComponent(): " + eventLogger);
		triggerEvent("EVENT_01");
	}

	public void unbindWriteLog(EventLogger eventLogger) {
		this.eventLogger = eventLogger;
		logger.fine("ConsumeLog.lostComponent(): " + eventLogger);
	}

	public void triggerEvent(String eventTxt) {
		EventLogger w = this.eventLogger;
		logger.info("-> Trigger Event '"+eventTxt+"'.");
		if (w != null)
			w.info(eventTxt, 123);
		else
			logger.info("WriteLog service is not available! :(    (eventTxt="+eventTxt+")");
	}

}
