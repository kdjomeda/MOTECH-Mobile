/**
 * MOTECH PLATFORM OPENSOURCE LICENSE AGREEMENT
 *
 * Copyright (c) 2010-11 The Trustees of Columbia University in the City of
 * New York and Grameen Foundation USA.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Grameen Foundation USA, Columbia University, or
 * their respective contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY GRAMEEN FOUNDATION USA, COLUMBIA UNIVERSITY
 * AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL GRAMEEN FOUNDATION
 * USA, COLUMBIA UNIVERSITY OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.motechproject.mobile.omp.manager.orserve;

import static org.easymock.EasyMock.*;

import org.motechproject.mobile.core.model.GatewayRequest;
import org.motechproject.mobile.core.model.GatewayRequestDetails;
import org.motechproject.mobile.core.model.GatewayRequestImpl;
import org.motechproject.mobile.core.model.GatewayResponse;
import org.motechproject.mobile.core.model.GatewayResponseImpl;
import org.motechproject.mobile.core.model.MStatus;
import org.motechproject.mobile.omp.manager.GatewayMessageHandler;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tst for th ORServeGatewayManagerImpl class
 *
 * @author Kofi A. Asamoah (yoofi@dreamoval.com)
 * Date Created Aug 10, 2009
 */
public class ORServeGatewayManagerImplTest {

    ORServeGatewayManagerImpl instance;
    GatewayMessageHandler mockHandler;
    GatewayRequestDetails mockGatewayRequestDetails ;
    public ORServeGatewayManagerImplTest() {
    }

    @Before
    public void setUp(){
        mockHandler = createMock(GatewayMessageHandler.class);
        mockGatewayRequestDetails = createMock(GatewayRequestDetails.class);
        mockGatewayRequestDetails.setId(32000000001l);
        instance = new ORServeGatewayManagerImpl();
        instance.setProductCode("testId");
        instance.setSenderId("Test Sender");
        instance.setMessageHandler(mockHandler);
    }

    /**
     * Test of sendMessage method, of class ORServeGatewayManagerImpl.
     */
    @Ignore
    @Test
    public void testSendMessage() {
        System.out.println("sendMessage");

        GatewayRequest messageDetails = new GatewayRequestImpl();
        messageDetails.setDateFrom(new Date());
        messageDetails.setMessage("a message for testing");
        messageDetails.setDateTo(new Date());
        messageDetails.setRecipientsNumber("000000000000");
        messageDetails.setGatewayRequestDetails(mockGatewayRequestDetails);

        expect(
                mockHandler.parseMessageResponse((GatewayRequest) anyObject(), (String) anyObject())
                ).andReturn(new HashSet<GatewayResponse>());
        replay(mockHandler);
        
        Set<GatewayResponse> result = instance.sendMessage(messageDetails);
        assertNotNull(result);
        verify(mockHandler);
    }

    /**
     * Test of getMessageStatus method, of class ORServeGatewayManagerImpl.
     */
    @Ignore
    @Test
    public void testGetMessageStatus() {
        System.out.println("getMessageStatus");        
        GatewayResponseImpl response = new GatewayResponseImpl();
        response.setGatewayMessageId("testId");

        String result = instance.getMessageStatus(response);
        System.out.println("Gateway Response: " + result);
        assertNotNull(result);
    }

    /**
     * Test of mapMessageStatus method, of class ClickatellGatewayManagerImpl.
     */
    @Ignore
    @Test
    public void testMapMessageStatus() {
        System.out.println("mapMessageStatus");

        GatewayResponseImpl response = new GatewayResponseImpl();
        response.setResponseText("Some gateway response message");
        
        expect(
                mockHandler.parseMessageStatus((String) anyObject())
                ).andReturn(MStatus.DELIVERED);
        replay(mockHandler);

        MStatus result = instance.mapMessageStatus(response);
        assertEquals(result, MStatus.DELIVERED);
        verify(mockHandler);
    }

}