package ch.wengle.demoapp.producerjms;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.ProducerTemplate;

import ch.wengle.demoapp.api.MsgProducer;
import ch.wengle.demoapp.api.msg.Msg;

public class ProducerJms implements MsgProducer {
	private ProducerTemplate producerTemplate;
	

	@Override
	public Response request(Msg msg) {
		Map<String, Object> headers = new HashMap<>();
		producerTemplate.sendBodyAndHeaders("direct:send-msg", msg.getBody(""), headers);
		return null;
	}
	
	
	public void setProducerTemplate(ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
	}
	
}
