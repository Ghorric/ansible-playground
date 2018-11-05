package ch.wengle.demoapp.api;

import ch.wengle.demoapp.api.msg.Msg;

public interface MsgProducer {

	public void request(Msg msg, Response response);

	@FunctionalInterface
	public static interface Response {
		public void received(Msg msg);
	}
}
