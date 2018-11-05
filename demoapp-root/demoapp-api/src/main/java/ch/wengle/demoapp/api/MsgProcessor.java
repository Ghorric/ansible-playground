package ch.wengle.demoapp.api;

import ch.wengle.demoapp.api.msg.Msg;

@FunctionalInterface
public interface MsgProcessor {
	public void process(Msg msg);
}
