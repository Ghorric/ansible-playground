package ch.wengle.demoapp.cmd;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.console.Session;

import ch.wengle.demoapp.api.msg.MsgFact;

public abstract class AbstractShellCmd implements Action {

	@Reference
	protected Session session;

	@Reference
	protected MsgFact msgFact;

	protected static <T> T throwIfNull(T obj, String errorMsg) {
		if (obj == null)
			throw new NullPointerException(errorMsg);
		return obj;
	}
}
