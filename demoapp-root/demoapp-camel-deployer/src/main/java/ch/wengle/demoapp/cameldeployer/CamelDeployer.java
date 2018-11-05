package ch.wengle.demoapp.cameldeployer;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.model.RoutesDefinition;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.MsgProcessor;
import ch.wengle.demoapp.api.msg.Msg;

public class CamelDeployer implements MsgProcessor {
	final Logger log = LoggerFactory.getLogger(CamelDeployer.class);

	private static final String GEN_RES_CAMEL = "/demoapp-res-1.0-SNAPSHOT-dynamic-camel-route.xml";
	private static final String CLS = CamelDeployer.class.getSimpleName();

	private BundleContext bundleContext;
	private CamelContext camelContext;
	private ProducerTemplate camelProducerTemplate;

	public void start() {
		try {
			camelContext.stop();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to stop camelContext: " + camelContext, e);
		}
		Bundle bundle = bundleContext.getBundle();
		try (InputStream is = bundle.getEntry(GEN_RES_CAMEL).openStream()) {
			RoutesDefinition routesDefinition = camelContext.loadRoutesDefinition(is);
			routesDefinition.getRoutes().forEach(rd -> {
				String routeId = rd.getId();
				try {
					camelContext.addRouteDefinition(rd);
					// camelContext.startRoute(routeId);
				} catch (Exception e) {
					throw new IllegalStateException("Failed to add or start the route: " + routeId, e);
				}
			});

			// Start and await
			CountDownLatch latch = new CountDownLatch(1);
			camelContext.addStartupListener((cc, as) -> {
				latch.countDown();
			});
			camelContext.start();
			log.debug("|--> Await camel startup: {}", showRoutes(camelContext));
			latch.await(5, TimeUnit.SECONDS);
			log.debug("|--> Loaded camel routes: {}", showRoutes(camelContext));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load file: " + GEN_RES_CAMEL, e);
		}
	}

	public void stop() {
		try {
			camelContext.removeRoute("dynamicRoute");
		} catch (Exception ex) {
			throw new RuntimeException("Failed to remove route 'dynamicRoute'.", ex);
		}
		log.debug("|--> Loaded camel routes: count={}", camelContext.getRoutes().size());
	}

	@Override
	public void process(Msg msg) {
		if (camelContext.getRoute("dynamicRoute") != null) {
			log.debug("|--> {} sendBody: {}", CLS, msg);
			camelProducerTemplate.sendBody("direct:dynamicIn", msg);
		} else
			log.warn("|--> Can't process '{}' because route is already stopped: {}", msg, showRoutes(camelContext));
	}

	private String showRoutes(CamelContext cc) {
		return cc.getRoutes().stream().map(r -> r.getId() + "(" + cc.getRouteStatus(r.getId()) + ")")
				.collect(Collectors.toList()).toString();
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	public void setCamelProducerTemplate(ProducerTemplate camelProducerTemplate) {
		this.camelProducerTemplate = camelProducerTemplate;
	}

}
