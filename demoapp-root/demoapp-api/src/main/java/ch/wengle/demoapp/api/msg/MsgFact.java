package ch.wengle.demoapp.api.msg;

import java.util.Map;

public interface MsgFact {
	public Msg create(String body, Map<HeaderKey, Object> headers);
	public Msg create(String body, HeaderBuilderProvider provider);
}
