package ch.wengle.demoapp.producerjms;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.MsgProducer;
import ch.wengle.demoapp.api.msg.Msg;

public class ProducerJms implements MsgProducer {
	final Logger log = LoggerFactory.getLogger(ProducerJms.class);

	private ProducerTemplate producerTemplate;

	@Override
	public void request(Msg msg, Response response) {
		Map<String, Object> headers = msg.getHeaders().entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey().name(), e -> e.getValue()));
		String respMsg = producerTemplate.requestBodyAndHeaders("direct:send-msg", msg.getBody(""), headers,
				String.class);
		response.received(msg.body(respMsg));
	}

	public void setProducerTemplate(ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
	}

}
