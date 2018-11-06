package ch.wengle.demoapp.util;

import ch.wengle.demoapp.api.MsgProcessor;
import ch.wengle.demoapp.api.msg.Msg;

public class CreateReplyMsg implements MsgProcessor {
	
	private String nodeName;

	@Override
	public void process(Msg msg) {
		msg.setBody(nodeName);
	}
	
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
