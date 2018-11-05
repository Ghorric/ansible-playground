package ch.wengle.demoapp.cmd;

import java.util.List;
import java.util.UUID;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.eventlogger.EventLogger;

@Command(scope = "demo-app", name = "event", description = "Send message")
@Service
public class ShellEvent extends AbstractShellCmd {
	final Logger log = LoggerFactory.getLogger(ShellEvent.class);

	@Option(name = "-m", aliases = {
			"--msg" }, description = "Body of the message", required = false, multiValued = false)
	private String msg;

	@Option(name = "-p", aliases = { "--param" }, description = "Message params", required = false, multiValued = true)
	private List<Object> params;

	@Reference
	private EventLogger eventLogger;

	@Override
	public Object execute() throws Exception {
		if (msg == null)
			msg = "HelloEvent";
		String uuid = UUID.randomUUID().toString();
		log.info("Send event (ID={}): {}", uuid, msg);
		throwIfNull(eventLogger, "eventLogger is null").info(b -> b.txt(msg, params));
		return null;
	}

}
