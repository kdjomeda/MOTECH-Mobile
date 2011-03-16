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

package org.motechproject.mobile.core.dao;

import org.motechproject.mobile.core.model.GatewayRequest;


import org.motechproject.mobile.core.model.MStatus;
import java.util.Date;
import java.util.List;


/**
 * Provides Generic CRUD functionalities inherited from {@link org.motechproject.mobile.core.dao.GenericDAO}
 * with additional Helper methods to manipulate {@link org.motechproject.mobile.core.model.GatewayRequest } objects
 * or Collection of objects
 *
 *  Date: Jul 29, 2009
 * @author Joseph Djomeda (joseph@dreamoval.com)
 */
public interface GatewayRequestDAO<T extends GatewayRequest> extends GenericDAO<T> {

    /**
     * Selects all GatewayRequest objects based on the passed {@link org.motechproject.mobile.core.model.MStatus}
     * @param status status to search for
     * @return List of GatewayRequest objects
     */
    public List<GatewayRequest> getByStatus(MStatus status);

    /**
     * Selects all GatewayRequest based on the {@link org.motechproject.mobile.core.model.MStatus} and the date passed
     * @param status the status to search for
     * @param date the date to search for
     * @return List of GatewayRequest objects
     */
    public List<GatewayRequest> getByStatusAndSchedule(MStatus status, Date date);
}
