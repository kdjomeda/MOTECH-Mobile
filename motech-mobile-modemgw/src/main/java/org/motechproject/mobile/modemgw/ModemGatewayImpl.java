package org.motechproject.mobile.modemgw;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.mobile.core.model.GatewayRequest;
import org.motechproject.mobile.core.model.GatewayResponse;
import org.motechproject.mobile.core.model.MStatus;
import org.motechproject.mobile.core.service.MotechContext;
import org.motechproject.mobile.imp.serivce.IMPService;
import org.motechproject.mobile.omp.manager.GatewayManager;
import org.motechproject.mobile.omp.manager.GatewayMessageHandler;
import org.motechproject.mobile.omp.manager.utils.MessageStatusStore;
import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.Message.MessageTypes;
import org.smslib.OutboundMessage.MessageStatuses;
import org.smslib.modem.SerialModemGateway;

/**
 * An implementation of both the sending and receiving side of the MoTeCH mobile
 * messaging interfaces, to work with a serial modem via smslib.
 * 
 * @author batkinson
 * 
 */
public class ModemGatewayImpl implements GatewayManager,
		IInboundMessageNotification, IOutboundMessageNotification {

	private Log log = LogFactory.getLog(ModemGatewayImpl.class);
	private Service service;
	private AGateway modemGateway;
	private GatewayMessageHandler messageHandler;
	private IMPService inboundService;

	private String modemId = "motech.modem";
	private String comPort = "COM5";
	private int baudRate = 115200;
	private String manufacturer = null;
	private String model = null;

	private boolean acceptIncoming = false;
	private boolean acceptOutgoing = true;

	private MessageStatusStore statusStore;

	public void setAcceptIncoming(boolean acceptIncoming) {
		this.acceptIncoming = acceptIncoming;
	}

	public void setAcceptOutgoing(boolean acceptOutgoing) {
		this.acceptOutgoing = acceptOutgoing;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setInboundService(IMPService inboundService) {
		this.inboundService = inboundService;
	}

	public void setModemId(String modemId) {
		this.modemId = modemId;
	}

	public void setStatusStore(MessageStatusStore statusStore) {
		this.statusStore = statusStore;
	}

	@SuppressWarnings("unchecked")
	public Set<GatewayResponse> sendMessage(GatewayRequest messageDetails,
			MotechContext context) {

		String requestId = messageDetails.getRequestId();

		if (log.isDebugEnabled())
			log.debug("queueing outbound message: id=" + requestId);

		if (acceptOutgoing) {

			OutboundMessage message = new OutboundMessage(messageDetails
					.getRecipientsNumber(), messageDetails.getMessage());

			// Use requestId for message id rather than generating another
			message.setId(requestId);

			service.queueMessage(message);

			String response = getSMSLibMessageStatus(message);

			Set<GatewayResponse> responses = messageHandler
					.parseMessageResponse(messageDetails, response, context);

			GatewayResponse responseObj = responses.iterator().next();

			// Update status store with response status of sent message
			statusStore.updateStatus(requestId, responseObj.getResponseText());

			return responses;
		}

		return null;
	}

	/**
	 * Initializes the gateway by starting the smslib service with a modem
	 * gateway configured with the set properties. It is called after properties
	 * are wired in.
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	void init() throws Exception {
		service = new Service();

		modemGateway = new SerialModemGateway(modemId, comPort, baudRate,
				manufacturer, model);

		modemGateway.setOutbound(acceptOutgoing);

		if (acceptOutgoing) {
			service.setOutboundMessageNotification(this);
		}

		modemGateway.setInbound(acceptIncoming);

		if (acceptIncoming) {
			service.setInboundMessageNotification(this);
		}

		service.addGateway(modemGateway);
		service.startService();
	}

	/**
	 * Tears down the gateway. This is called before the application shuts down.
	 * 
	 * @throws Exception
	 */
	@PreDestroy
	void shutdown() throws Exception {
		service.stopService();
	}

	public void setMessageHandler(GatewayMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public GatewayMessageHandler getMessageHandler() {
		return this.messageHandler;
	}

	public MStatus mapMessageStatus(GatewayResponse response) {
		String responseText = response.getResponseText();
		return messageHandler.parseMessageStatus(responseText);
	}

	/**
	 * Used by the SMS messaging service code to query the status of a PENDING
	 * message. Normally, the call queries a remote gateway, but we fake it with
	 * a simple dictionary.
	 */
	public String getMessageStatus(GatewayResponse response) {
		String status = statusStore.getStatus(response.getGatewayMessageId());
		return status;
	}

	/**
	 * Converts the state of an SMSLib message to a string that can be mapped to
	 * the motech-mobile MStatus values.
	 * 
	 * @param msg
	 * @return
	 */
	private String getSMSLibMessageStatus(OutboundMessage msg) {
		MessageStatuses msgStatus = msg.getMessageStatus();
		String responseString = msgStatus.toString();
		if (msgStatus == MessageStatuses.FAILED) {
			responseString = "ERROR_" + msg.getFailureCause().toString();
		}
		return responseString;
	}

	/**
	 * Called when an incoming message is detected. It is responsible for
	 * handing off incoming messages to the inbound message processor and
	 * removing the message from the modem's memory.
	 */
	public void process(String gatewayId, MessageTypes msgType,
			InboundMessage msg) {

		// No requestId for inbound messages, using smslib id
		long msgId = msg.getMessageId();

		if (log.isDebugEnabled())
			log.debug("receiving inbound message: id=" + msgId);

		if (acceptIncoming && inboundService != null) {
			try {
				inboundService.processRequest(msg.getText(), msg
						.getOriginator(), false);

				service.deleteMessage(msg);

				if (log.isDebugEnabled())
					log.debug("removed inbound message: id=" + msgId);
			} catch (Throwable t) {
				log.error("inbound message failure: id=" + msgId, t);
			}
		}
	}

	/**
	 * Gets called when a queued outbound message is handled.
	 */
	public void process(String gatewayId, OutboundMessage msg) {

		String status = getSMSLibMessageStatus(msg);

		// Update the status of the message after processing
		statusStore.updateStatus(msg.getId(), status);

		if (log.isDebugEnabled())
			log.debug("outbound message processed: id=" + msg.getId()
					+ ", status=" + status.toString());
	}
}