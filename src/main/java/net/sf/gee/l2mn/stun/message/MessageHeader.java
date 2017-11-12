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
import java.nio.charset.StandardCharsets;

import net.sf.gee.l2mn.stun.util.StunUtil;
import net.sf.gee.logger.factory.GLogFactory;
import net.sf.gee.logger.log.SimpleGLogger;

/**
 * RFC-5389 - https://tools.ietf.org/html/rfc5389#section-6
 * 
 * All STUN messages MUST start with a 20-byte header followed by zero or more Attributes. The STUN
 * header contains a STUN message type, magic cookie, transaction ID, and message length.
 * 
 * <pre>
 * {@code
 *   0                   1                   2                   3
 *   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |0 0|     STUN Message Type     |         Message Length        |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                         Magic Cookie                          |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *   |                                                               |
 *   |                     Transaction ID (96 bits)                  |
 *   |                                                               |
 *   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *     }
 * </pre>
 *
 * The most significant 2 bits of every STUN message MUST be zeroes. This can be used to
 * differentiate STUN packets from other protocols when STUN is multiplexed with other protocols on
 * the same port.
 * 
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public final class MessageHeader implements Serializable {

  private static final long serialVersionUID = 1L;

  private MessageTypeEnum type = null;

  private int length = 0;

  /**
   * The magic cookie field MUST contain the fixed value 0x2112A442 in network byte order.
   * 
   * In RFC 3489 [RFC3489], this field was part of the transaction ID; placing the magic cookie in
   * this location allows a server to detect if the client will understand certain attributes that
   * were added in this revised specification. In addition, it aids in distinguishing STUN packets
   * from packets of other protocols when STUN is multiplexed with those other protocols on the same
   * port.
   */
  private long magicCookie = 0x2112A442;

  private String transactionId = null;

  public MessageHeader() {
    super();
  }

  /**
   * @param type
   */
  public MessageHeader(MessageTypeEnum type) {
    this();
    this.type = type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (magicCookie ^ (magicCookie >>> 32));
    result = prime * result + ((transactionId == null) ? 0 : transactionId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MessageHeader other = (MessageHeader) obj;
    if (magicCookie != other.magicCookie)
      return false;
    if (transactionId == null) {
      if (other.transactionId != null)
        return false;
    }
    else if (!transactionId.equals(other.transactionId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MessageHeader [");
    if (type != null)
      builder.append("type=").append(type).append(", ");
    builder.append("length=").append(length).append(", magicCookie=").append(magicCookie)
        .append(", transactionId=").append(transactionId).append("]");
    return builder.toString();
  }

  public MessageTypeEnum getType() {
    return type;
  }

  public void setType(MessageTypeEnum type) {
    this.type = type;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public long getMagicCookie() {
    return magicCookie;
  }

  public void setMagicCookie(long magicCookie) {
    this.magicCookie = magicCookie;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public byte[] getBytes() throws IOException {

    final SimpleGLogger logger =
        GLogFactory.getInstance().getLogger(SimpleGLogger.class, Message.class);

    // write message
    try (ByteArrayOutputStream bout = new ByteArrayOutputStream();) {
      // header
      bout.write(0);
      bout.write(StunUtil.integerToOneByte(this.type.getValue()));
      bout.write(StunUtil.integerToTwoBytes(this.length));
      bout.write(StunUtil.longToFourBytes(this.magicCookie));
      bout.write(this.transactionId.getBytes(StandardCharsets.UTF_8));

      // right size
      if (bout.size() == 20) {
        return bout.toByteArray();

      } // less than 20 bytes
      else if (bout.size() < 20) {

        for (int i = bout.size(); i < 20; i++) {
          bout.write(0);
        }

        return bout.toByteArray();

      } // more than 20 bytes
      else {
        return StunUtil.subArray(bout.toByteArray(), 0, 20);
      }

    }
    catch (IOException e) {
      logger.logError(e);

      throw e;
    }
  }

  /**
   * @param header
   * 
   * @throws IOException
   */
  public static MessageHeader parse(byte[] header) throws IOException {

    if (header == null || header.length < 1) {
      return null;
    }

    final MessageHeader msgHeader = new MessageHeader();

    // type
    msgHeader.type = MessageTypeEnum
        .getValueByValue(StunUtil.twoBytesToInteger(new byte[] {header[0], header[1]}));

    // length
    msgHeader.length = StunUtil.twoBytesToInteger(new byte[] {header[2], header[3]});

    // magic cookie
    msgHeader.magicCookie =
        StunUtil.fourBytesToLong(new byte[] {header[4], header[5], header[6], header[7]});

    // transaction id
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

      // iterate
      for (int i = 8; i < header.length; i++) {

        if (header[i] != 0x0) {
          out.write(header[i]);
        }
      }

      // to byte array
      msgHeader.transactionId = new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
    catch (Exception e) {
    }

    return msgHeader;
  }

}
