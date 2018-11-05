package ch.wengle.demoapp.api.msg;

import java.util.Map;
import java.util.Optional;

public interface Msg {

	public void putHeader(HeaderKey key, Object value);

	public Msg header(HeaderKey key, Object value);

	public Map<HeaderKey, Object> getHeaders();

	public <T> Optional<T> getHeader(HeaderKey key, Class<T> type);

	public default <T> T getHeaderOrThrow(HeaderKey key, Class<T> type) {
		return getHeader(key, type).orElseThrow();
	}

	public <T> T getHeader(HeaderKey key, T defaultValue);

	public default Optional<String> getHeaderStr(HeaderKey key) {
		return getHeader(key, String.class);
	}

	public default String getHeaderStr(HeaderKey key, String defaultValue) {
		return getHeader(key, defaultValue);
	}

	public default String getHeaderStrOrThrow(HeaderKey key) {
		return getHeaderStr(key).orElseThrow();
	}

	public void setBody(String body);

	public Msg body(String body);

	public Optional<String> getBody();

	public String getBodyOrThrow();

	public String getBody(String defaultValue);
}