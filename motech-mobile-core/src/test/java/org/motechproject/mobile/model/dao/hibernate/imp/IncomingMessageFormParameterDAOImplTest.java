package org.motechproject.mobile.model.dao.hibernate.imp;

import org.motechproject.mobile.core.manager.CoreManager;
import org.motechproject.mobile.core.model.IncomingMessageFormParameter;
import org.motechproject.mobile.core.model.IncomingMessageFormParameterImpl;
import org.motechproject.mobile.core.service.MotechContext;
import org.motechproject.mobile.model.dao.imp.IncomingMessageFormParameterDAO;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author joseph Djomeda (joseph@dreamoval.com)
 * @Date Dec 11, 2009
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/test-core-config.xml"})
public class IncomingMessageFormParameterDAOImplTest {

    @Autowired
    CoreManager coreManager;
    IncomingMessageFormParameterDAO imfDAO;
    @Autowired
    private IncomingMessageFormParameter imfp1;
    @Autowired
    private IncomingMessageFormParameter imfp2;
    @Autowired
    private IncomingMessageFormParameter imfp3;

    public IncomingMessageFormParameterDAOImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        MotechContext tc = coreManager.createMotechContext();
        imfDAO = coreManager.createIncomingMessageFormParameterDAO(tc);


        imfp1.setId(987L);
        imfp1.setDateCreated(new Date());
        imfp1.setErrCode(23);
        imfp1.setErrText("error text right?");

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of save method, of class IncomingMessageFormParameterDAOImpl.
     */
    @Test
    public void testSave() {
        System.out.println("IncomingMessageFormParameterDAOImpl save");
        Session session = ((Session) imfDAO.getDBSession().getSession());
        Transaction tx = session.beginTransaction();
        imfDAO.save(imfp1);
        tx.commit();


        IncomingMessageFormParameter fromdb = (IncomingMessageFormParameter) session.get(IncomingMessageFormParameterImpl.class, imfp1.getId());

        Assert.assertNotNull(fromdb);
        Assert.assertEquals(fromdb, imfp1);
        Assert.assertEquals(fromdb.getId(), imfp1.getId());
        System.out.println("the form parameter error text: " + fromdb.getErrText());
        System.out.println(fromdb.toString());

    }
}