package ch.wengle.demoapp.api;

import ch.wengle.demoapp.api.msg.Msg;

public interface MsgProducer {
	
	public Response request(Msg msg);
	
	@FunctionalInterface
	public interface Response{
		public void receivedResponse(Msg msg);
	}
}
