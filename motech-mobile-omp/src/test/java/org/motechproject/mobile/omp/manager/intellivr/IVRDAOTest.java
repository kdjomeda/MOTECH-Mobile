package org.motechproject.mobile.omp.manager.intellivr;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mobile.core.model.GatewayRequest;
import org.motechproject.mobile.core.model.GatewayRequestImpl;
import org.motechproject.mobile.core.model.Language;
import org.motechproject.mobile.core.model.LanguageImpl;
import org.motechproject.mobile.core.model.MStatus;
import org.motechproject.mobile.core.model.MessageRequest;
import org.motechproject.mobile.core.model.MessageRequestImpl;
import org.motechproject.mobile.core.model.MessageType;
import org.motechproject.mobile.core.model.NotificationType;
import org.motechproject.mobile.core.model.NotificationTypeImpl;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = {"classpath:META-INF/ivrdaotest-config.xml"})
public class IVRDAOTest {

	@Resource
	IntellIVRDAO ivrDao;
	@Resource
	SessionFactory sessionFactory;
	
	String userid1 = "111111111";
	String phone1 = "5555555551";
	String userid2 = "222222222";
	String phone2 = "5555555552";
	String userid3 = "333333333";
	String phone3 = "5555555553";
	String language = "English";
	Language english;
	NotificationType n1,n2,n3;
	MessageRequest mr1,mr2,mr3;
	GatewayRequest r1,r2,r3;
	
	Log log = LogFactory.getLog(IVRDAOTest.class);
	
	@Before
	public void setUp() throws Exception {
		
		english = new LanguageImpl();
		english.setCode("en");
		english.setId(29000000001l);
		english.setName("English");
		
		n1 = new NotificationTypeImpl();
		n1.setId(1L);
		n1.setDescription("testing");
		n1.setName("testing");
		
		mr1 = new MessageRequestImpl();
		mr1.setId(29000000002l);
		mr1.setLanguage(english);
		mr1.setRecipientId(userid1);
		mr1.setRequestId("mr1");
		mr1.setMessageType(MessageType.VOICE);
		mr1.setNotificationType(n1);
		mr1.setPhoneNumberType("PERSONAL");
	
		r1 = new GatewayRequestImpl();
		r1.setId(29000000003l);
		r1.setMessageRequest(mr1);
		r1.setMessageStatus(MStatus.PENDING);
		r1.setRecipientsNumber(phone1);

		n2 = new NotificationTypeImpl();
		n2.setId(2L);

		mr2 = new MessageRequestImpl();
		mr2.setId(29000000004l);
		mr2.setLanguage(english);
		mr2.setRecipientId(userid2);
		mr2.setRequestId("mr2");
		mr2.setMessageType(MessageType.VOICE);
		mr2.setNotificationType(n2);
		mr2.setPhoneNumberType("PERSONAL");
			
		r2 = new GatewayRequestImpl();
		r2.setId(29000000005l);
		r2.setMessageRequest(mr2);
		r2.setMessageStatus(MStatus.PENDING);
		r2.setRecipientsNumber(phone2);
		
		n3 = new NotificationTypeImpl();
		n3.setId(3L);
		
		mr3 = new MessageRequestImpl();
		mr3.setId(29000000006l);
		mr3.setLanguage(english);
		mr3.setRecipientId(userid3);
		mr3.setRequestId("mr3");
		mr3.setMessageType(MessageType.VOICE);
		mr3.setNotificationType(n3);
		mr3.setPhoneNumberType("PERSONAL");
			
		r3 = new GatewayRequestImpl();
		r3.setId(29000000007l);
		r3.setMessageRequest(mr3);
		r3.setMessageStatus(MStatus.PENDING);
		r3.setRecipientsNumber(phone3);

	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	@Transactional
	public void testSaveIVRCallSession() {
		
		IVRCallSession expectedSession = new IVRCallSession(userid1,phone1,language,"OUT",0,0,IVRCallSession.OPEN,UUID.randomUUID().toString());
		
		expectedSession.getGatewayRequests().add(r1);
		
		IVRCall call = new IVRCall(new Date(), null, null, 0);
		IVRCallStatusEvent cse1 = new IVRCallStatusEvent(IVRCallStatus.REQUESTED, new Date(), "");
		IVRCallStatusEvent cse2 = new IVRCallStatusEvent(IVRCallStatus.COMPLETED, new Date(), "");
		call.getCallStatusEvents().add(cse1);
		call.getCallStatusEvents().add(cse2);
		IVRMenu menu = new IVRMenu("test.wav", new Date(), 60, "1", null);
		call.getMenus().add(menu);
		expectedSession.getCalls().add(call);		
		
		long id = ivrDao.saveIVRCallSession(expectedSession);
		
		IVRCallSession sessionFromDatabase = ivrDao.loadIVRCallSession(id);
		
		assertEquals(expectedSession, sessionFromDatabase);
		assertTrue(sessionFromDatabase.getGatewayRequests().contains(r1));
		assertFalse(sessionFromDatabase.getGatewayRequests().contains(r2));
		
	}
	
	@Test
	@Transactional
	public void testLoadIVRCallSessionByState() {
		
		Integer[] states = { IVRCallSession.OPEN, IVRCallSession.SEND_WAIT };
		
		List<IVRCallSession> databaseSessions = ivrDao.loadIVRCallSessionsByState(states);
		assertTrue(databaseSessions.size() == 0);
		
		IVRCallSession expectedSession1 = new IVRCallSession(userid1,phone1,language,"OUT",0,0,IVRCallSession.OPEN,UUID.randomUUID().toString());	
		expectedSession1.getGatewayRequests().add(r1);

		IVRCallSession expectedSession2 = new IVRCallSession(userid2,phone2,language,"OUT",0,0,IVRCallSession.SEND_WAIT,UUID.randomUUID().toString());	
		expectedSession2.getGatewayRequests().add(r2);

		IVRCallSession expectedSession3 = new IVRCallSession(userid3,phone3,language,"OUT",0,0,IVRCallSession.REPORT_WAIT,UUID.randomUUID().toString());	
		expectedSession3.getGatewayRequests().add(r3);
		
		ivrDao.saveIVRCallSession(expectedSession1);
		ivrDao.saveIVRCallSession(expectedSession2);
		ivrDao.saveIVRCallSession(expectedSession3);
		
		databaseSessions = ivrDao.loadIVRCallSessionsByState(states);
		
		assertTrue(databaseSessions.contains(expectedSession1));
		assertTrue(databaseSessions.contains(expectedSession2));
		assertFalse(databaseSessions.contains(expectedSession3));
		
	}

	@Test
	@Transactional
	public void testLoadIVRCallSessionsByUserPhoneAndState(){

		Integer[] states = { IVRCallSession.OPEN, IVRCallSession.SEND_WAIT };
		
		IVRCallSession expectedSession1 = new IVRCallSession(userid1,phone1,language,"OUT",0,0,IVRCallSession.OPEN,UUID.randomUUID().toString());	
		expectedSession1.getGatewayRequests().add(r1);

		IVRCallSession expectedSession2 = new IVRCallSession(userid2,phone2,language,"OUT",0,0,IVRCallSession.SEND_WAIT,UUID.randomUUID().toString());	
		expectedSession2.getGatewayRequests().add(r2);

		IVRCallSession expectedSession3 = new IVRCallSession(userid3,phone3,language,"OUT",0,0,IVRCallSession.REPORT_WAIT,UUID.randomUUID().toString());	
		expectedSession3.getGatewayRequests().add(r3);
		
		ivrDao.saveIVRCallSession(expectedSession1);
		ivrDao.saveIVRCallSession(expectedSession2);
		ivrDao.saveIVRCallSession(expectedSession3);
		
		List<IVRCallSession> databaseSessions = ivrDao.loadIVRCallSessionsByUserPhoneAndState(userid1,phone1,states);
		
		assertTrue(databaseSessions.contains(expectedSession1));
		assertFalse(databaseSessions.contains(expectedSession2));
		assertFalse(databaseSessions.contains(expectedSession3));
		
	}
	
	@Test
	@Transactional
	public void testLoadIVRCallSessionByExternalId(){

		String externalId = UUID.randomUUID().toString();
		
		IVRCallSession expectedSession = new IVRCallSession(userid1,phone1,language,"OUT",0,0,IVRCallSession.OPEN,externalId);	
		expectedSession.getGatewayRequests().add(r1);

		ivrDao.saveIVRCallSession(expectedSession);
		
		IVRCallSession databaseSession = ivrDao.loadIVRCallSessionByExternalId(externalId);
		
		assertEquals(expectedSession, databaseSession);
		
	}
	
}
