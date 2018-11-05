package ch.wengle.demoapp.api.msg;

import ch.wengle.demoapp.api.event.EventKey;

public enum Header implements HeaderKey, EventKey {
	// Msg
	MSG_ID, MSG_SENDER,

	// JMS
	JMS_DESTINATION, JMS_CORRELATION_ID, JMS_MSG_ID, JMS_PRIORITY, JMS_REDELIVERED,

	// Event
	SEVERITY, EVENT_TXT, MSG_BODY, EXCEPTION
}