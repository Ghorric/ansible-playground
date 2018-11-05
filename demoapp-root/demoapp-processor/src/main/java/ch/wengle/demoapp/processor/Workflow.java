package ch.wengle.demoapp.processor;

import static ch.wengle.demoapp.api.msg.Header.MSG_ID;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.MsgProcessor;
import ch.wengle.demoapp.api.eventlogger.EventLogger;
import ch.wengle.demoapp.api.msg.Msg;

@Component(service = MsgProcessor.class, property = "type=workflow")
public class Workflow implements MsgProcessor {
	final Logger log = LoggerFactory.getLogger(Workflow.class);
	private static final Object CLS = Workflow.class.getSimpleName();

	EventLogger eventLogger;
	MsgProcessor dynamicRoute;

	@Activate
	void activate(Map<String, Object> properties) {
		logOsgiEvent("activate", null, properties);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
	public void bindEventLogger(EventLogger eventLogger) {
		this.eventLogger = eventLogger;
		logOsgiEvent("gotComponent", eventLogger, null);
	}

	public void unbindEventLogger(EventLogger eventLogger) {
		if (Objects.requireNonNull(eventLogger).equals(this.eventLogger))
			this.eventLogger = eventLogger;
		logOsgiEvent("lostComponent", eventLogger, null);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY, target = "(type=dynamic-route)")
	public void bindDynamicRoute(MsgProcessor dynamicRoute, Map<String, Object> properties) {
		this.dynamicRoute = dynamicRoute;
		logOsgiEvent("gotComponent", dynamicRoute, properties);
	}

	public void unbindDynamicRoute(MsgProcessor dynamicRoute, Map<String, Object> properties) {
		if (Objects.requireNonNull(dynamicRoute).equals(this.dynamicRoute))
			this.dynamicRoute = dynamicRoute;
		logOsgiEvent("lostComponent", dynamicRoute, properties);
	}

	@Override
	public void process(Msg msg) {
		event(msg, "Workflow starts processing for msg '{}'.", msg.getHeaderStrOrThrow(MSG_ID));
		dynamicRoute(msg);
		event(msg, "Workflow completed processing for msg '{}'.", msg.getHeaderStrOrThrow(MSG_ID));
	}

	public void dynamicRoute(Msg msg) {
		call(dynamicRoute, s -> s.process(msg));
	}

	public void event(Msg msg, String eventTxt, Object... eventTxtArgs) {
		call(eventLogger, s -> s.info(b -> b.msg(msg).txt(eventTxt, eventTxtArgs)));
	}

	protected void logOsgiEvent(String osgiEvent, Object obj, Map<String, Object> properties) {
		log.debug("{}.{}({}): {}", CLS, osgiEvent, objToStr(dynamicRoute), properties);
	}

	protected String objToStr(Object obj) {
		return obj == null ? "null" : obj.getClass().getSimpleName() + ": " + obj;
	}

	protected <T> void call(T service, Consumer<T> serviceProvider) {
		T s = service; // Prevents NullPointer when OSGi service disappears
		if (s != null)
			serviceProvider.accept(s);
		else
			log.info("Service is not available! :(");
	}

}
