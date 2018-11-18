package ch.wengle.demoapp.cmd;

import static ch.wengle.demoapp.api.msg.Header.MSG_ID;
import static ch.wengle.demoapp.api.msg.Header.MSG_SENDER;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.wengle.demoapp.api.MsgProducer;
import ch.wengle.demoapp.api.msg.MsgFact;

@Command(scope = "demo-app", name = "send", description = "Send message")
@Service
public class ShellSendMsg extends AbstractShellCmd {
	private static final Logger log = LoggerFactory.getLogger(ShellSendMsg.class);
	private static final String CLS = ShellSendMsg.class.getSimpleName();
	private static final String UNAVAILABLE = "The OSGi service '" + MsgProducer.class.getSimpleName()
			+ "' is unavailable. Is a MsgProducer bundle installed?";

	@Option(name = "-m", aliases = {
			"--msg" }, description = "Body of the message (default=HelloJmsMsg)", required = false, multiValued = false)
	private String msg;

	@Option(name = "-n", aliases = {
			"--numberOfMsgs" }, description = "Sends the same message as many times as specified (default=1)", required = false, multiValued = false)
	private Integer numberOfMsgs;

	@Option(name = "-t", aliases = {
			"--timeout" }, description = "Timeout in MILLISECONDS to wait for all response messages (default=60000)", required = false, multiValued = false)
	private Long timeout;
	
	@Option(name = "-s", aliases = {
	"--sysout" }, description = "Print output", required = false, multiValued = false)
	private boolean sysout;

	@Option(name = "-l", aliases = {
	"--log" }, description = "Log output", required = false, multiValued = false)
	private boolean logout;	
	
	@Reference(optional = true)
	private MsgProducer msgProducer;

	@Override
	public Object execute() throws Exception {
		if (msg == null)
			msg = "HelloJmsMsg";
		if (numberOfMsgs == null || numberOfMsgs < 1)
			numberOfMsgs = 1;
		if (timeout == null)
			timeout = 60000L;

		Optional<CountDownLatch> optCountDownLatch = timeout < 1 ? Optional.empty()
				: Optional.of(new CountDownLatch(numberOfMsgs));
		BlockingQueue<String> allRespMsgs = new LinkedBlockingQueue<>(numberOfMsgs);
		for (int i = 0; i < numberOfMsgs; i++)
			sendMsg(msgFact, msgProducer, numberOfMsgs > 1 ? msg + " (" + i + ")" : msg, optCountDownLatch, allRespMsgs);
		String outp = optCountDownLatch.map(latch -> {
			await(latch, timeout);
			return allRespMsgs.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		}).orElse(new HashMap<String, Long>()).toString();
		if(sysout) 
			System.out.println(outp);
		if(logout) 
			log.info(outp);
		return outp;
	}

	protected void sendMsg(MsgFact msgFact, MsgProducer msgProducer, String msg, Optional<CountDownLatch> latch,
			BlockingQueue<String> allRespMsgs) {
		String uuid = UUID.randomUUID().toString();
		log.info("Send msg (ID={}): {}", uuid, msg);
		throwIfNull(msgProducer, UNAVAILABLE).request(throwIfNull(msgFact, "msgFact").create(msg, p -> {
			p.hdr(MSG_ID, uuid);
			p.hdr(MSG_SENDER, CLS);
		}), (resp, ex) -> {
			if (ex != null)
				log.error("Failure '{}': ", resp.getHeaderStr(MSG_ID, ""), ex);
			else
				log.info("Received response message '{}': {}", resp.getHeaderStr(MSG_ID, ""), resp.getBody(""));
			allRespMsgs.offer(ex != null ? "Exception" : resp.getBodyOrThrow());
			latch.ifPresent(l -> l.countDown());
		});
	}

	protected void await(CountDownLatch countDownLatch, Long timeout) {
		try {
			if (!countDownLatch.await(timeout, TimeUnit.MILLISECONDS))
				throw new IllegalStateException("Failed to await the latch. Timeout=" + timeout);
		} catch (InterruptedException ex) {
			throw new IllegalStateException("Failed to await the latch. Timeout=" + timeout, ex);
		}
	}
}
