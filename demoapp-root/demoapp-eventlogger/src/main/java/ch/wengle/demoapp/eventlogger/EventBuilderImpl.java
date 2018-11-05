package ch.wengle.demoapp.eventlogger;

import static ch.wengle.demoapp.api.msg.Header.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

import ch.wengle.demoapp.api.event.EventKey;
import ch.wengle.demoapp.api.eventlogger.EventBuilder;
import ch.wengle.demoapp.api.msg.Header;
import ch.wengle.demoapp.api.msg.Msg;

public class EventBuilderImpl implements EventBuilder {
	private static final String PLACEHOLDER = "{}";
	private Map<EventKey, String> data;

	public EventBuilderImpl() {
		this(new HashMap<>());
	}

	protected EventBuilderImpl(Map<EventKey, String> data) {
		this.data = data;
	}

	@Override
	public EventBuilder txt(String txt, Object... txtParams) {
		data.put(EVENT_TXT, txt == null || txtParams == null || txtParams.length < 1 ? (txt==null?"":txt)
				: replacePlaceholders(new StringBuffer(txt), new LinkedList<>(Arrays.asList(txtParams)), PLACEHOLDER, 0)
						.toString());
		return this;
	}

	protected StringBuffer replacePlaceholders(StringBuffer full, Queue<Object> params, String placeholder,
			int startSearchFromIndex) {
		if (Objects.requireNonNull(full).length() < 1 || Objects.requireNonNull(params).size() < 1)
			return full;
		int i = full.indexOf(placeholder, startSearchFromIndex);
		if (i < 0)
			return full;
		Object el = params.poll();
		return replacePlaceholders(full.replace(i, i + 2, el == null ? "null" : el.toString()), params, placeholder, i);
	}

	@Override
	public EventBuilder msg(Msg msg) {
		if (msg == null)
			return this;
		put(MSG_BODY, msg.getBody());
		put(MSG_ID, msg.getHeaderStrOrThrow(MSG_ID));
		return put(MSG_SENDER, msg.getHeaderStr(Header.MSG_SENDER, "unknown"));
	}

	@Override
	public EventBuilder property(EventKey key, Object obj) {
		return put(key, obj);
	}

	@Override
	public EventBuilder properties(Map<EventKey, Object> properties) {
		if (properties == null)
			return this;
		properties.forEach((k, v) -> property(k, v));
		return this;
	}

	@Override
	public EventBuilder properties(Exception exception) {
		throw new IllegalStateException("Not yet implemented!");
	}

	protected EventBuilderImpl put(EventKey key, Object value) {
		data.put(key, value == null ? "null" : value.toString());
		return this;
	}

	public Map<EventKey, String> getData() {
		return data;
	}

}
