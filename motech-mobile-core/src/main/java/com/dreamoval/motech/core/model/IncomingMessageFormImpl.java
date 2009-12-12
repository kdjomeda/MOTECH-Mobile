package com.dreamoval.motech.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * IncomingMessageFormImpl is the implementation of the IncomingMessageForm interface
 * which is the actually mapped class in the hibernate.It provides properties to handle IncomingMessageForm operations
 *
 * Date: Dec 03, 2009
 * @author Joseph Djomeda (joseph@dreamoval.com)
 */
public class IncomingMessageFormImpl extends MotechEntityImpl implements IncomingMessageForm {

    private IncomingMessageFormDefinition incomingMsgFormDefinition;
    private Date dateCreated;
    private Date lastModified;
    private IncMessageFormStatus messageFormStatus;
    private List<IncomingMessageFormParameter> incomingMsgFormParameters = new ArrayList<IncomingMessageFormParameter>();

    /**
     * @return the incomingMsgFormDefinition
     */
    public IncomingMessageFormDefinition getIncomingMsgFormDefinition() {
        return incomingMsgFormDefinition;
    }

    /**
     * @param incomingMsgFormDefinition the incomingMsgFormDefinition to set
     */
    public void setIncomingMsgFormDefinition(IncomingMessageFormDefinition incomingMsgFormDefinition) {
        this.incomingMsgFormDefinition = incomingMsgFormDefinition;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the lastModified
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * @param lastModified the lastModified to set
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * @return the messageStatus
     */
    public IncMessageFormStatus getMessageFormStatus() {
        return messageFormStatus;
    }

    /**
     * @param messageFormStatus the messageStatus to set
     */
    public void setMessageFormStatus(IncMessageFormStatus messageFormStatus) {
        this.messageFormStatus = messageFormStatus;
    }

    /**
     * @return the incomingMsgFormParameters
     */
    public List<IncomingMessageFormParameter> getIncomingMsgFormParameters() {
        return incomingMsgFormParameters;
    }

    /**
     * @param incomingMsgFormParameters the incomingMsgFormParameters to set
     */
    public void setIncomingMsgFormParameters(List<IncomingMessageFormParameter> incomingMsgFormParameters) {
        this.incomingMsgFormParameters = incomingMsgFormParameters;
    }

    
    
}
