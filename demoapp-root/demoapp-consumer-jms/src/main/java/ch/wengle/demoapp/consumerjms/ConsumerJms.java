package ch.wengle.demoapp.consumerjms;

import static ch.wengle.demoapp.api.msg.Header.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.jms.JmsConstants;

import ch.wengle.demoapp.api.MsgProcessor;
import ch.wengle.demoapp.api.msg.Msg;
import ch.wengle.demoapp.api.msg.MsgFact;

public class ConsumerJms implements Processor {

	protected List<MsgProcessor> msgProcessors;
	protected MsgFact msgFact;

	@Override
	public void process(Exchange ex) throws Exception {
		Msg msg = createMsg(ex);
		msgProcessors.stream().forEach(proc -> proc.process(msg));
		createResponse(ex, msg);
	}

	protected Msg createMsg(Exchange ex) {
		Message m = ex.getIn();
		return msgFact.create(Objects.requireNonNull(m.getBody(String.class), "body"), builder -> {
			builder.hdr(MSG_ID, Objects.requireNonNull(m.getHeader(MSG_ID.name()), MSG_ID.name()));
			builder.hdr(MSG_SENDER, m.getHeader(MSG_SENDER.name()));
			builder.hdr(JMS_DESTINATION, m.getHeader(JmsConstants.JMS_DESTINATION_NAME));
			builder.hdr(JMS_CORRELATION_ID, m.getHeader("JMSCorrelationID"));
			builder.hdr(JMS_MSG_ID, m.getHeader("JMSMessageID"));
			builder.hdr(JMS_PRIORITY, m.getHeader("JMSPriority"));
			builder.hdr(JMS_REDELIVERED, m.getHeader("JMSRedelivered"));
		});
	}

	protected void createResponse(Exchange ex, Msg msg) {
		Message camelMsg = ex.getIn();
		camelMsg.setBody(msg.getBody(""));
		camelMsg.setHeaders(msg.getHeaders().entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey().name(), e -> e.getValue())));
	}

	public void setMsgFact(MsgFact msgFact) {
		this.msgFact = msgFact;
	}

	public void setMsgProcessors(List<MsgProcessor> msgProcessors) {
		this.msgProcessors = msgProcessors;
	}
}