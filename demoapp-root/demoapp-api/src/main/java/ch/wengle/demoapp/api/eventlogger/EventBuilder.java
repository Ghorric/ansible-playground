package ch.wengle.demoapp.api.eventlogger;

import java.util.Map;

import ch.wengle.demoapp.api.event.EventKey;
import ch.wengle.demoapp.api.msg.Msg;

public interface EventBuilder {

	public EventBuilder txt(String txt, Object... txtParams);

	public EventBuilder msg(Msg msg);

	public EventBuilder property(EventKey key, Object obj);

	public EventBuilder properties(Map<EventKey, Object> properties);

	public EventBuilder properties(Exception exception);
}
