package ch.wengle.demoapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ch.wengle.demoapp.api.msg.HeaderBuilder;
import ch.wengle.demoapp.api.msg.HeaderBuilderProvider;
import ch.wengle.demoapp.api.msg.HeaderKey;
import ch.wengle.demoapp.api.msg.Msg;
import ch.wengle.demoapp.api.msg.MsgFact;

public class MsgFactImpl implements MsgFact {
	private LocalFact localFact;

	public MsgFactImpl() {
		this(new LocalFact());
	}

	protected MsgFactImpl(LocalFact localFact) {
		this.localFact = localFact;
	}

	@Override
	public Msg create(String body, Map<HeaderKey, Object> headers) {
		return new MsgImpl(body, headers);
	}

	@Override
	public Msg create(String body, HeaderBuilderProvider provider) {
		Msg msg = localFact.msg(Objects.requireNonNull(body));
		Objects.requireNonNull(provider).provide(localFact.headerBuilder(msg));
		return msg;
	}

	public static final class HeaderBuilderImpl implements HeaderBuilder {
		private Msg msg;

		public HeaderBuilderImpl(Msg msg) {
			this.msg = msg;
		}

		@Override
		public HeaderBuilder hdr(HeaderKey key, Object obj) {
			msg.header(key, obj);
			return this;
		}

	}

	public static final class MsgImpl implements Msg {
		protected Optional<String> body;
		protected final Map<HeaderKey, Object> headers;

		@Override
		public void putHeader(HeaderKey key, Object value) {
			header(key, value);
		}

		@Override
		public Msg header(HeaderKey key, Object value) {
			this.headers.put(Objects.requireNonNull(key), Optional.ofNullable(value));
			return this;
		}

		public MsgImpl(String body, Map<HeaderKey, Object> headers) {
			this.body = Optional.ofNullable(body);
			this.headers = Objects.requireNonNull(headers);
		}

		@Override
		public Map<HeaderKey, Object> getHeaders() {
			return headers;
		}

		@Override
		public <T> Optional<T> getHeader(HeaderKey key, Class<T> type) {
			Object val = getHeaders().get(Objects.requireNonNull(key));
			return Optional.ofNullable(unsafeCast(val, Objects.requireNonNull(type)));
		}

		@Override
		public <T> T getHeader(HeaderKey key, T defaultValue) {
			return unsafeCast(
					getHeaders().getOrDefault(Objects.requireNonNull(key), Objects.requireNonNull(defaultValue)));
		}

		@Override
		public void setBody(String body) {
			body(body);
		}

		@Override
		public Msg body(String body) {
			this.body = Optional.ofNullable(body);
			return this;
		}

		@Override
		public Optional<String> getBody() {
			return body;
		}

		public String getBodyOrThrow() {
			return getBody().orElseThrow();
		}

		@Override
		public String getBody(String defaultValue) {
			return getBody().orElse(defaultValue);
		}

		@SuppressWarnings("unchecked")
		protected <T> T unsafeCast(Object obj, Class<T> type) {
			return (T) obj;
		}

		@SuppressWarnings("unchecked")
		protected <T> T unsafeCast(Object obj) {
			return (T) obj;
		}

	}

	protected static final class LocalFact {

		public Msg msg(String body) {
			return new MsgImpl(body, new HashMap<>());
		}

		public HeaderBuilder headerBuilder(Msg msg) {
			return new HeaderBuilderImpl(msg);
		}

	}

}
