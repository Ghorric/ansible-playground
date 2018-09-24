package ch.wengle.demoapp.util;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;

public abstract class OsgiServiceCaller<T> {
	public static Logger logger = Logger.getLogger(OsgiServiceCaller.class.getName());

	protected BundleContext bundleContext;

	public void callService(Object body) {
		Class<T> type = getType();
		Optional.ofNullable(bundleContext.getServiceReference(type)).map(sr -> bundleContext.getService(sr))
				.ifPresentOrElse(service -> {
					getServiceConsumer().accept(service, body);
				}, () -> {
					logger.warning("Failed to get a service for the interface '" + type + "' for the body: " + body);
				});
	}

	protected abstract Class<T> getType();

	protected abstract BiConsumer<T, Object> getServiceConsumer();

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}
