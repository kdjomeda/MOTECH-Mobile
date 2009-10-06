package com.dreamoval.motech.omi.service;

import com.dreamoval.motech.core.model.Language;
import com.dreamoval.motech.core.model.MessageType;
import com.dreamoval.motech.core.model.NotificationType;
import java.util.Date;
import java.util.List;

/**
 * Provides external access to OMI methods
 *
 * @author Kofi A. Asamoah (yoofi@dremoval.com)
 * Date Created: Jul 31, 2009
 */
public interface OMIService {

    /**
     * Sends a message to a CHPS patient
     *
     * @param messageId Numeric key of the message to send
     * @param serviceDate Date of current service delivery
     * @param patientNumber Patient mobile contact number
     * @param langCode Code for the recipient's preferred language
     * @param patientNumberType Type of contact number. Possible values include PERSONAL, SHARED
     * @param notificationType Type of notification to be sent
     * @param messageType Preferred message type. Possible values include TEXT, VOICE
     * @return The id of the message sent
     */
    public String savePatientMessageRequest(Long messageId, String patientName, String patientNumber, ContactNumberType patientNumberType, String langCode, MessageType messageType, Long notificationType, Date startDate, Date endDate);

    /**
     * Sends a message to a CHPS Worker
     * 
     * @param messageId Numeric key of the message to send
     * @param workerName Name of CHPS worker recieving message
     * @param workerNumber Worker mobile contact number
     * @param patientList A List of patients requiring service from CHPS worker
     * @return The id of the message sent
     */
    public String saveCHPSMessageRequest(Long messageId, String workerName, String workerNumber, List<PatientImpl> patientList, String langCode, MessageType messageType, Long notificationType, Date startDate, Date endDate);
    
    /**
     * Processes stored MessageRequests into GatewayRequests and schedules them for delivery on the OMP
     */
    public void processMessageRequests();
    
    /**
     * Resends all messages that have been marked for retry
     */
    public void processMessageRetries();
    
    /**
     * Queries and updates the statuses of sent messages
     */
    public void getMessageResponses();
}