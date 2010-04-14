package org.motechproject.mobile.omp.manager.intellivr;


import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mobile.core.manager.CoreManager;
import org.motechproject.mobile.core.model.GatewayRequest;
import org.motechproject.mobile.core.model.GatewayRequestImpl;
import org.motechproject.mobile.core.model.GatewayResponse;
import org.motechproject.mobile.core.model.GatewayResponseImpl;
import org.motechproject.mobile.core.model.MStatus;
import org.motechproject.mobile.core.service.MotechContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = {"classpath:META-INF/test-omp-config.xml"})
public class IntellIVRGatewayMessageHandlerTest {

	@Resource
	IntellIVRGatewayMessageHandler intellIVRMessageHandler;
	CoreManager coreManager;
	
	Map<String, MStatus> statusCodes = new HashMap<String, MStatus>();
	
	@Before
	public void setUp() throws Exception {
		
		statusCodes.put("0000", MStatus.CANCELLED);
		statusCodes.put("0001", MStatus.CANCELLED);
		statusCodes.put("0002", MStatus.CANCELLED);
		statusCodes.put("0003", MStatus.CANCELLED);
		statusCodes.put("0004", MStatus.CANCELLED);
		statusCodes.put("0005", MStatus.CANCELLED);
		statusCodes.put("0006", MStatus.CANCELLED);
		statusCodes.put("0007", MStatus.CANCELLED);
		statusCodes.put("0008", MStatus.CANCELLED);
		statusCodes.put("0009", MStatus.CANCELLED);
		statusCodes.put("0010", MStatus.CANCELLED);
		statusCodes.put("0011", MStatus.CANCELLED);
		statusCodes.put("OK", MStatus.PENDING);
		statusCodes.put("COMPLETED", MStatus.DELIVERED);
		statusCodes.put("REJECTED", MStatus.FAILED);
		statusCodes.put("BUSY", MStatus.FAILED);
		statusCodes.put("CONGESTION", MStatus.FAILED);
		statusCodes.put("NOANSWER", MStatus.FAILED);
		statusCodes.put("INTERNALERROR", MStatus.FAILED);
		
	}
	
	@Test
	public void testLookupStatus() {
		
		for ( String code : statusCodes.keySet())
			assertEquals(statusCodes.get(code), intellIVRMessageHandler.lookupStatus(code));

	}

	@Test
	public void testLookupResponse() {
		
		for ( String code : statusCodes.keySet())
			assertEquals(statusCodes.get(code), intellIVRMessageHandler.lookupResponse(code));
		
	}

	@Test
	public void testParseMessageStatus() {
		
		for ( String code : statusCodes.keySet()) 
			assertEquals(statusCodes.get(code), intellIVRMessageHandler.parseMessageStatus(code));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testParseMessageResponse() {
		
		String testRequestID = "testRequestID";
		String testPhone = "15555555555";
		String testMessage = "test message";
				
		GatewayRequest message = new GatewayRequestImpl();
		message.setRequestId(testRequestID);
		message.setRecipientsNumber(testPhone);
		message.setMessage(testMessage);
		
		for ( String code : statusCodes.keySet()) {

			GatewayResponse gwResponse = new GatewayResponseImpl();
			gwResponse.setId(1L);

			MotechContext context = createMock(MotechContext.class);
			
			coreManager = createMock(CoreManager.class);
			intellIVRMessageHandler.setCoreManager(coreManager);
			
			expect(coreManager.createGatewayResponse(context)).andReturn(gwResponse);			
			replay(coreManager);
			
			Set<GatewayResponse> responses = intellIVRMessageHandler.parseMessageResponse(message, code, context);
			
			for ( GatewayResponse response : responses ) {
				assertTrue(response.getDateCreated()!= null);
				assertEquals(message, response.getGatewayRequest());
				assertEquals(testRequestID, response.getGatewayMessageId());
				assertEquals(statusCodes.get(code), response.getMessageStatus());
				assertEquals(testPhone, response.getRecipientNumber());
				assertEquals(testRequestID, response.getRequestId());
				assertEquals(code, response.getResponseText());
			}
			
			verify(coreManager);
			
		}
		
		
	}

	
}