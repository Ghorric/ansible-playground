package ch.wengle.demoapp.cmd;

import static ch.wengle.demoapp.api.msg.Header.ID;

import java.util.UUID;
import java.util.logging.Logger;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.Session;

import ch.wengle.demoapp.api.MsgProducer;
import ch.wengle.demoapp.api.msg.MsgFact;

@Command(scope = "demo-app", name = "send", description = "Send message")
@Service
public class ShellSendMsg implements Action {
	public static Logger logger = Logger.getLogger(ShellSendMsg.class.getName());

	@Option(name = "-m", aliases = {
			"--msg" }, description = "Body of the message", required = false, multiValued = false)
	private String msg;

	@Reference
	private Session session;

	@Reference
	private MsgProducer msgProducer;

	@Reference
	private MsgFact msgFact;

	@Override
	public Object execute() throws Exception {
		if(msg == null)
			msg = "HelloWorld";
		String uuid = UUID.randomUUID().toString();
		logger.info("Send msg (ID=" + uuid + "): " + msg);
		throwIfNull(msgProducer, "msgProducer is null").request(throwIfNull(msgFact, "msgFact").create(msg, p -> p.hdr(ID, uuid)));
		return null;
	}
	
	public static <T> T throwIfNull(T obj, String errorMsg) {
		if(obj == null)
			throw new NullPointerException(errorMsg);
		return obj;
	}

}
