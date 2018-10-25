package ch.wengle.demoapp.util;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.osgi.framework.BundleContext;

public abstract class OsgiServiceCaller<T> {

	protected BundleContext bundleContext;

	public void callService(Object body) {
		Class<T> type = getType();
		Optional.ofNullable(bundleContext.getServiceReference(type)).map(sr -> bundleContext.getService(sr))
				.ifPresent(service -> getServiceConsumer().accept(service, body));
	}

	protected abstract Class<T> getType();

	protected abstract BiConsumer<T, Object> getServiceConsumer();

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

}
