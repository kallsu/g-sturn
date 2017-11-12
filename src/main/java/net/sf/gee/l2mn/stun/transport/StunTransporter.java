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
package net.sf.gee.l2mn.stun.transport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import net.sf.gee.l2mn.stun.message.Message;
import net.sf.gee.l2mn.stun.message.MessageHeader;
import net.sf.gee.l2mn.stun.message.attribute.MessageAttribute;
import net.sf.gee.l2mn.stun.message.attribute.MessageAttributeType;
import net.sf.gee.l2mn.stun.message.attribute.XorMappedAddress;
import net.sf.gee.l2mn.stun.util.StunUtil;
import net.sf.gee.logger.factory.GLogFactory;
import net.sf.gee.logger.log.SimpleGLogger;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class StunTransporter implements Transporter {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final SimpleGLogger LOGGER =
      GLogFactory.getInstance().getLogger(SimpleGLogger.class, StunTransporter.class);

  private final DatagramSocket client;

  /**
   * @throws SocketException
   * 
   */
  public StunTransporter(String host, int port) throws SocketException {
    super();

    InetSocketAddress hostAddress = new InetSocketAddress(host, port);
    this.client = new DatagramSocket();
    this.client.connect(hostAddress);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.Closeable#close()
   */
  @Override
  public void close() throws IOException {

    if (this.client != null) {
      this.client.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.sf.gee.l2mn.stun.transport.StunTransporter#doBind(net.sf.gee.l2mn.stun.message.Message)
   */
  @Override
  public Message doBind(Message request) throws IOException {

    if (request == null || request.getHeader() == null || request.getHeader().getBytes() == null
        || request.getHeader().getBytes().length == 0) {

      LOGGER.logError("Request is null or header is empty.");

      throw new IOException("request is null or header is empty");
    }

    // get header
    byte[] data = request.getHeader().getBytes();
    DatagramPacket dp = new DatagramPacket(data, data.length);

    LOGGER.logDebug("Try to send .... ");

    // send
    client.send(dp);

    LOGGER.logDebug("Sent.");

    // prepare data
    byte[] receiveData = new byte[1024];
    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

    // receive
    client.receive(receivePacket);
    LOGGER.logDebug("Received: [%s]", receivePacket.getData().length);

    // get header
    byte[] header = StunUtil.subArray(receivePacket.getData(), 0, 19);

    LOGGER.logDebug("Parse header.");

    // parse header
    MessageHeader msgHeader = MessageHeader.parse(header);

    // check error
    if (msgHeader.getType().isError()) {
      LOGGER.logWarn("Message Header is an error.");

      return new Message(msgHeader);
    }

    LOGGER.logDebug(msgHeader.toString());

    // get body
    byte[] body = StunUtil.subArray(receivePacket.getData(), 20, (20 + msgHeader.getLength()));

    LOGGER.logDebug("Body [%s]", body.length);

    // parse response
    List<MessageAttribute> attrs = parseResponse(body);

    // check
    if (attrs.isEmpty()) {
      return new Message(msgHeader);
    }

    // response message
    Message response = new Message(msgHeader);

    // add all
    response.getAttributes().addAll(attrs);

    return response;
  }

  /**
   * Parse response.
   * 
   * @param body byte array of packet body
   * 
   * @return {@linkplain java.util.List} of {@link MessageAttribute}
   * 
   * @throws IOException
   */
  private List<MessageAttribute> parseResponse(byte[] body) throws IOException {

    final List<MessageAttribute> attrs = new LinkedList<>();

    // attribute type
    int attrType = StunUtil.twoBytesToInteger(new byte[] {body[0], body[1]});

    // attribute length
    int attrLength = StunUtil.twoBytesToInteger(new byte[] {body[2], body[3]});

    if (attrType == MessageAttributeType.XOR_MAPPED_ADDRESS.getCode()) {

      // add to list
      attrs.add(new XorMappedAddress(attrLength, StunUtil.subArray(body, 4, 4 + attrLength)));
    }

    return attrs;
  }


}
