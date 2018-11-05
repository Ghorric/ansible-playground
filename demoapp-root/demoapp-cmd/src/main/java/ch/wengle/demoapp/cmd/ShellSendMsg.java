package ch.wengle.demoapp.cmd;

import static ch.wengle.demoapp.api.msg.Header.MSG_ID;
import static ch.wengle.demoapp.api.msg.Header.MSG_SENDER;

import java.util.UUID;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.MsgProducer;

@Command(scope = "demo-app", name = "send", description = "Send message")
@Service
public class ShellSendMsg extends AbstractShellCmd {
	final Logger log = LoggerFactory.getLogger(ShellSendMsg.class);
	private static final String UNAVAILABLE = "The OSGi service '" + MsgProducer.class.getSimpleName()
			+ "' is unavailable. Is a MsgProducer bundle installed?";
	private static final String CLS = ShellSendMsg.class.getSimpleName();

	@Option(name = "-m", aliases = {
			"--msg" }, description = "Body of the message", required = false, multiValued = false)
	private String msg;

	@Reference(optional = true)
	private MsgProducer msgProducer;

	@Override
	public Object execute() throws Exception {
		if (msg == null)
			msg = "HelloJmsMsg";
		String uuid = UUID.randomUUID().toString();
		log.info("Send msg (ID={}): {}", uuid, msg);
		throwIfNull(msgProducer, UNAVAILABLE).request(throwIfNull(msgFact, "msgFact").create(msg, p -> {
			p.hdr(MSG_ID, uuid);
			p.hdr(MSG_SENDER, CLS);
		}), responseMsg -> log.info("Received response message '{}': {}", responseMsg.getHeaderStr(MSG_ID, ""), responseMsg.getBody("")));
		return null;
	}

}
