package ch.wengle.demoapp.api;

import ch.wengle.demoapp.api.msg.Msg;

public interface MsgProcessor {

	public Msg process(Msg msg);

}
