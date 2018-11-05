package ch.wengle.demoapp.eventlogger;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import ch.wengle.demoapp.api.event.EventKey;
import ch.wengle.demoapp.api.msg.Header;

public class EventBuilderImplTest {
	private static final EventKey TXT = Header.EVENT_TXT;
	private static final String PH = "{}";

	EventBuilderImpl sut;
	Map<EventKey, String> data;

	@Before
	public void before() {
		data = new HashMap<>();
		sut = new EventBuilderImpl(data);
	}

	@Test
	public void txt_txtIsNull() {
		sut.txt(null, "a");
		assertEquals("", data.get(TXT));
	}

	@Test
	public void txt_txtIsEmptyStr() {
		sut.txt("", "a");
		assertEquals("", data.get(TXT));
	}

	@Test
	public void txt_tooManyArgs() {
		sut.txt("{}-{}-{}", "a", "b", "c", "d");
		assertEquals("a-b-c", data.get(TXT));
	}

	@Test
	public void txt_tooFewArgs() {
		sut.txt("{}-{}-{}", "a");
		assertEquals("a-{}-{}", data.get(TXT));
	}

	@Test
	public void txt_3Args() {
		sut.txt("{}-{}-{}", "a", "b", "c");
		assertEquals("a-b-c", data.get(TXT));
	}

	@Test
	public void txt_noArgs() {
		sut.txt("a-b-c");
		assertEquals("a-b-c", data.get(TXT));
	}

	@Test
	public void txt_nullArg() {
		sut.txt("a-{}-c", (String) null);
		assertEquals("a-null-c", data.get(TXT));
	}

	@Test
	public void replacePlaceholders_variousCases() {
		assertEquals("a-b-c", sut.replacePlaceholders(sb("a-b-c"), params(), PH, 0).toString());
		assertEquals("a-b-c", sut.replacePlaceholders(sb("a-b-c"), params("x"), PH, 0).toString());
		assertEquals("a-b-c", sut.replacePlaceholders(sb("{}-b-c"), params("a"), PH, 0).toString());
		assertEquals("a-b-c", sut.replacePlaceholders(sb("a-{}-c"), params("b"), PH, 0).toString());
		assertEquals("a-b-c", sut.replacePlaceholders(sb("a-b-{}"), params("c"), PH, 0).toString());
		assertEquals("a-b-c", sut.replacePlaceholders(sb("{}-{}-{}"), params("a", "b", "c"), PH, 0).toString());
		assertEquals("a-b-c", sut.replacePlaceholders(sb("{}-{}-{}"), params("a", "b", "c", "d"), PH, 0).toString());
		assertEquals("a-b-{}", sut.replacePlaceholders(sb("{}-{}-{}"), params("a", "b"), PH, 0).toString());
		assertEquals("a", sut.replacePlaceholders(sb("{}"), params("a"), PH, 0).toString());
		assertEquals("{}", sut.replacePlaceholders(sb("{}"), params(), PH, 0).toString());
		assertEquals("null", sut.replacePlaceholders(sb("{}"), params((String) null), PH, 0).toString());
		assertEquals("null", sut.replacePlaceholders(sb("{}"), params(null, null), PH, 0).toString());
		assertEquals("ab", sut.replacePlaceholders(sb("{}{}"), params("a", "b"), PH, 0).toString());
	}

	private StringBuffer sb(String str) {
		return str == null ? null : new StringBuffer(str);
	}

	private Queue<Object> params(String... txtParams) {
		return new LinkedList<>(Arrays.asList(txtParams));
	}
}
