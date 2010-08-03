/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.jms.reply;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.camel.component.jms.MessageSentCallback;

/**
 * Callback to be used when using the option <tt>useMessageIDAsCorrelationID</tt>.
 * <p/>
 * This callback will keep the correlation registration in {@link ReplyManager} up-to-date with
 * the <tt>JMSMessageID</tt> which was assigned and used when the message was sent.
 *
 * @version $Revision$
 */
public class UseMessageIdAsCorrelationIdMessageSentCallback implements MessageSentCallback {

    private ReplyManager replyManager;
    private String correlationId;
    private long requestTimeout;

    public UseMessageIdAsCorrelationIdMessageSentCallback(ReplyManager replyManager, String correlationId, long requestTimeout) {
        this.replyManager = replyManager;
        this.correlationId = correlationId;
        this.requestTimeout = requestTimeout;
    }

    public void sent(Session session, Message message, Destination destination) {
        String newCorrelationID = null;
        try {
            newCorrelationID = message.getJMSMessageID();
        } catch (JMSException e) {
            // ignore
        }
        if (newCorrelationID != null) {
            replyManager.updateCorrelationId(correlationId, newCorrelationID, requestTimeout);
        }
    }
}
