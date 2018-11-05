package ch.wengle.demoapp.api.event;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Event {
	private final Map<EventKey, String> values;

	public Event(Map<EventKey, String> values) {
		this.values = Collections.unmodifiableMap(Objects.requireNonNull(values));
	}

	public String get(EventKey key) {
		return values.get(Objects.requireNonNull(key));
	}

	public Map<EventKey, String> getAll() {
		return values;
	}

	public Set<EventKey> getKeys() {
		return values.keySet();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event [values=" + values + "]";
	}

}
