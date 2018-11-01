package ch.wengle.demoapp.processor;

import java.util.Map;
import java.util.logging.Logger;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import ch.wengle.demoapp.api.EventLogger;
import ch.wengle.demoapp.api.MsgProcessor;
import ch.wengle.demoapp.api.msg.Msg;

@Component(service=MsgProcessor.class)
public class MsgProcessorImpl implements MsgProcessor {
	public static Logger logger = Logger.getLogger(MsgProcessorImpl.class.getName());

	EventLogger eventLogger;

	@Activate
	void activate(Map<String, Object> properties) {
		logger.fine("ConsumeLog.activate(): " + properties);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
	public void bindEventLogger(EventLogger eventLogger) {
		this.eventLogger = eventLogger;
		logger.fine("EventLogger.gotComponent(): " + eventLogger);
	}

	public void unbindEventLogger(EventLogger eventLogger) {
		this.eventLogger = eventLogger;
		logger.fine("EventLogger.lostComponent(): " + eventLogger);
	}

	@Override
	public Msg process(Msg msg) {
		String eventTxt = msg.getBody("empty-body");
		EventLogger w = this.eventLogger; // Prevents NullPointer when OSGi service disappears
		logger.info("-> Trigger Event '" + eventTxt + "'.");
		if (w != null)
			w.info(eventTxt, 123);
		else
			logger.info("Service is not available! :(    (eventTxt=" + eventTxt + ")");
		return msg;
	}

}
