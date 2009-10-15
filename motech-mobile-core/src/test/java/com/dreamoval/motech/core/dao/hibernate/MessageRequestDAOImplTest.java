package com.dreamoval.motech.core.dao.hibernate;

import com.dreamoval.motech.core.dao.LanguageDAO;
import com.dreamoval.motech.core.dao.MessageRequestDAO;
import com.dreamoval.motech.core.dao.NotificationTypeDAO;
import com.dreamoval.motech.core.manager.CoreManager;
import com.dreamoval.motech.core.model.Language;
import com.dreamoval.motech.core.model.MStatus;
import com.dreamoval.motech.core.model.MessageRequest;
import com.dreamoval.motech.core.model.MessageRequestImpl;
import com.dreamoval.motech.core.model.MessageType;
import com.dreamoval.motech.core.model.NotificationType;
import com.dreamoval.motech.core.service.MotechContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  Date : Sep 25, 2009
 * @author joseph Djomeda (joseph@dreamoval.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-core-config.xml"})
public class MessageRequestDAOImplTest {

    MessageType t = MessageType.TEXT;
    MStatus sta = MStatus.PENDING;

    public MessageRequestDAOImplTest() {
    }
    @Autowired
    private CoreManager coreManager;
    @Autowired
    private MessageRequest mr1;
    @Autowired
    private MessageRequest mr2;
    @Autowired
    private MessageRequest mr3;
    @Autowired
    private Language lg1;
    @Autowired
    private NotificationType nt1;
    MessageRequestDAO mrDAO;
    LanguageDAO lDAO;
    NotificationTypeDAO ntDao;
    Date datefrom1;
    Date datefrom2;
    Date dateto1;
    Date dateto2;
    Date schedule;

    List expResult;

    @Before
    public void setUp() {

        MotechContext mc = coreManager.createMotechContext();
        lDAO = coreManager.createLanguageDAO(mc);
        lg1.setId(23L);
        lg1.setCode("es");

        ntDao = coreManager.createNotificationTypeDAO(mc);
        nt1.setId(9878L);
        nt1.setName("pregnancy");

        

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
             datefrom1 = df.parse("2009-08-21");
             datefrom2 = df.parse("2009-09-04");
             dateto1 = df.parse("2009-10-30");
            dateto2 = df.parse("2009-11-04");
            schedule = df.parse("2009-10-02");
        } catch (ParseException e) {
            e.printStackTrace();
        }




        mrDAO = coreManager.createMessageRequestDAO(mc);
        mr1.setId(1L);
        mr1.setDateCreated(new Date());
        mr1.setLanguage(lg1);
        mr1.setRecipientName("jlkj");
        mr1.setMessageType(t);

        mr2.setId(3L);
        mr2.setDateCreated(new Date());
        mr2.setLanguage(lg1);
        mr2.setRecipientName("jojo");
        mr2.setMessageType(t);
        mr2.setDateFrom(datefrom1);
        mr2.setDateTo(dateto1);
        mr2.setStatus(sta);
        mr2.setSchedule(schedule);

        mr3.setId(5L);
        mr3.setDateCreated(new Date());
        mr3.setLanguage(lg1);
        mr3.setRecipientName("joseph");
        mr3.setMessageType(t);
        mr3.setDateFrom(datefrom2);
        mr3.setDateTo(dateto2);
        mr3.setStatus(sta);
        mr3.setSchedule(schedule);

        Session session = (Session) lDAO.getDBSession().getSession();
        Transaction tx = session.beginTransaction();
        lDAO.save(lg1);
        ntDao.save(nt1);
        mrDAO.save(mr2);
        mrDAO.save(mr3);
        tx.commit();

        expResult  = new ArrayList();
        expResult.add(mr2);
        expResult.add(mr3);


    }

    /**
     * Test of save method, of class MessageRequestDAOImpl.
     */
    @Test
    public void testSave() {
        System.out.println("test save MessageRequest Object");
        Session session = (Session) mrDAO.getDBSession().getSession();
        Transaction tx = session.beginTransaction();
        mrDAO.save(mr1);
        tx.commit();

        MessageRequest fromdb = (MessageRequestImpl) session.get(MessageRequestImpl.class, mr1.getId());

        Assert.assertNotNull(fromdb);
        Assert.assertEquals(t, fromdb.getMessageType());
        Assert.assertEquals(lg1.getCode(), fromdb.getLanguage().getCode());
    }

    /**
     * Test of getMsgRequestByStatusAndSchedule method, of class MessageRequestDAOImpl.
     */
    @Test
    public void testGetMsgRequestByStatusAndSchedule() {
        System.out.println("getMsgRequestByStatusAndSchedule");
        
        List result = mrDAO.getMsgRequestByStatusAndSchedule(sta, schedule);
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult.size(), result.size());
        Assert.assertTrue(result.contains(mr2));
        Assert.assertTrue(result.contains(mr3));
        Assert.assertEquals(expResult, result);

    }
}
