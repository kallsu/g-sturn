/**
 * ------------------------------------------------------------------------------------------------
 *
 * Copyright 2015 - Giorgio Desideri
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 **/
package net.sf.gee.l2mn.stun.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.gee.l2mn.stun.message.attribute.MessageAttribute;
import net.sf.gee.logger.factory.GLogFactory;
import net.sf.gee.logger.log.SimpleGLogger;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  private final MessageHeader header;

  private final List<MessageAttribute> attributes;

  /**
   * @param header
   * @param payload
   */
  public Message(MessageHeader header) {
    super();

    this.header = header;
    this.attributes = new ArrayList<>(0);
  }

  public void addAttribute(final MessageAttribute attr) {
    if (attr != null) {
      attributes.add(attr);
    }
  }

  public MessageHeader getHeader() {
    return header;
  }

  public List<MessageAttribute> getAttributes() {
    return attributes;
  }

  public byte[] getAttributesAsBytes() throws IOException {

    final SimpleGLogger logger =
        GLogFactory.getInstance().getLogger(SimpleGLogger.class, Message.class);

    try (ByteArrayOutputStream bout = new ByteArrayOutputStream();) {

      // iterate on payload of packet
      for (MessageAttribute current : this.attributes) {
        // write the content
        bout.write(current.getValue());
      }

      // return byte array
      return bout.toByteArray();
    }
    catch (IOException e) {
      logger.logError(e);

      throw e;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Message [");
    if (header != null)
      builder.append("header=").append(header).append(", ");
    if (attributes != null) {
      builder.append("attributes=[");

      for (MessageAttribute current : attributes) {
        builder.append(current.toString());
      }

      builder.append("]");
    }
    builder.append("]");
    return builder.toString();
  }



}
