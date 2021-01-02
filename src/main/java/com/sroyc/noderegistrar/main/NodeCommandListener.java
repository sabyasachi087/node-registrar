package com.sroyc.noderegistrar.main;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sroyc.noderegistrar.exception.CommandExecutionFailuer;
import com.sroyc.noderegistrar.exception.ListenerNotFound;
import com.sroyc.noderegistrar.repo.NodeCommandRequest;
import com.sroyc.noderegistrar.repo.NodeCommandRequest.CommunicationStatus;
import com.sroyc.noderegistrar.repo.NodeCommandRequestRepository;
import com.sroyc.noderegistrar.repo.NodeRegistrationRepository;

@Component
public class NodeCommandListener implements Heartbeat, ApplicationListener<ContextRefreshedEvent>, DisposableBean {

	private static final Logger LOGGER = Logger.getLogger(NodeCommandListener.class.getCanonicalName());

	private final ScheduledExecutorService listenerExecutor = Executors.newScheduledThreadPool(1);

	private CommandListener listener;
	private NodeCommandRequestRepository ncrRepo;
	private NodeRegistrationRepository nodeRepo;
	private NodeResolver nodeResolver;
	private ApplicationContext context;

	@Autowired
	public NodeCommandListener(NodeCommandRequestRepository ncrRepo, NodeRegistrationRepository nodeRepo,
			NodeResolver nodeResolver, ApplicationContext context) {
		super();
		this.ncrRepo = ncrRepo;
		this.nodeRepo = nodeRepo;
		this.nodeResolver = nodeResolver;
		this.context = context;
	}

	protected void commandExecutor() {
		try {
			List<NodeCommandRequest> requests = this.ncrRepo.findByNodeIdAndStatus(NodeConstant.NODE_ID,
					CommunicationStatus.PUBLISHED);
			if (!CollectionUtils.isEmpty(requests) && listener != null) {
				requests.forEach(this::executeRequest);
			} else if (listener == null) {
				throw new ListenerNotFound("Listener is unavailable");
			}
		} catch (Throwable ex) {
			LOGGER.log(Level.SEVERE, "Could not complete command listener executor", ex);
		} finally {
			this.beat(LocalDateTime.now(Clock.systemUTC()));
			this.nodeResolver.resolve();
		}
	}

	protected void executeRequest(NodeCommandRequest request) {
		request = this.update(request, CommunicationStatus.CONSUMED);
		try {
			this.listener.listen(request.getCommand());
			this.update(request, CommunicationStatus.COMPLETED);
		} catch (CommandExecutionFailuer e) {
			LOGGER.log(Level.SEVERE, "Fail to execute command", e);
			this.update(request, CommunicationStatus.FAILED);
		}
	}

	protected NodeCommandRequest update(NodeCommandRequest request, CommunicationStatus status) {
		request.setLastUpdated(LocalDateTime.now());
		request.setStatus(status);
		return this.ncrRepo.updateStatus(request);
	}

	@Override
	public void beat(LocalDateTime beatTime) {
		try {
			this.nodeRepo.heartbeat(NodeConstant.NODE_ID, LocalDateTime.now(Clock.systemUTC()));
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Unable to serve heartbeat", ex);
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			this.listener = this.context.getBean(CommandListener.class);
			listenerExecutor.scheduleAtFixedRate(this::commandExecutor, 1, 5, TimeUnit.MINUTES);
			LOGGER.log(Level.INFO, "Listener has been registered and running ... ");
		} catch (Exception ex) {
			throw new ListenerNotFound(ex);
		}

	}

	@Override
	public void destroy() throws Exception {
		LOGGER.log(Level.INFO, "Stopping command listener ... ");
		this.listenerExecutor.shutdown();
		this.listenerExecutor.awaitTermination(1, TimeUnit.MINUTES);

	}

	@FunctionalInterface
	public static interface CommandListener {
		void listen(String command) throws CommandExecutionFailuer;

	}
}
