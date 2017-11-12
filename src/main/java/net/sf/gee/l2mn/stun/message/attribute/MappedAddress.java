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
package net.sf.gee.l2mn.stun.message.attribute;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import net.sf.gee.l2mn.stun.util.StunUtil;
import net.sf.gee.logger.factory.GLogFactory;
import net.sf.gee.logger.log.SimpleGLogger;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 * 
 *         The MAPPED-ADDRESS attribute indicates a reflexive transport address of the client. It
 *         consists of an 8-bit address family and a 16-bit port, followed by a fixed-length value
 *         representing the IP address. If the address family is IPv4, the address MUST be 32 bits.
 *         If the address family is IPv6, the address MUST be 128 bits. All fields must be in
 *         network byte order.
 * 
 *         <pre>
 * {@code
 *   The format of the MAPPED-ADDRESS attribute is:
 *
 *     0                   1                   2                   3
 *       0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *      |0 0 0 0 0 0 0 0|    Family     |           Port                |
 *      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *      |                                                               |
 *      |                 Address (32 bits or 128 bits)                 |
 *      |                                                               |
 *      +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *               Figure 5: Format of MAPPED-ADDRESS Attribute
 *
 *   The address family can take on the following values:
 *
 *   0x01:IPv4
 *   0x02:IPv6
 *     }
 *         </pre>
 * 
 *         The first 8 bits of the MAPPED-ADDRESS MUST be set to 0 and MUST be ignored by receivers.
 *         These bits are present for aligning parameters on natural 32-bit boundaries.
 * 
 */
public class MappedAddress extends MessageAttribute {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private int start = -1;

  private int family = -1;

  private int port = -1;

  private InetAddress address = null;

  /**
   * 
   */
  public MappedAddress() {
    super();

    super.setType(MessageAttributeType.MAPPED_ADDRESS.getCode());

    // starter
    this.start = 0;
  }

  /**
   * @param address
   * @param port
   */
  public MappedAddress(Inet4Address address, int port) {
    this();

    // family
    this.family = 0x01;

    // port
    this.port = port;

    // IPv4 address
    this.address = address;

    init();
  }

  /**
   * @param address
   * @param port
   */
  public MappedAddress(Inet6Address address, int port) {
    this();

    // family
    this.family = 0x02;

    // port
    this.port = port;

    // IPv6
    this.address = address;

    init();
  }

  protected void init() {

    final SimpleGLogger logger =
        GLogFactory.getInstance().getLogger(SimpleGLogger.class, MappedAddress.class);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

      out.write(start);
      out.write(family);
      out.write(StunUtil.integerToTwoBytes(port));
      out.write(address.getAddress());

      setValue(out.toByteArray());

    }
    catch (IOException e) {
      logger.logError(e);
    }
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getFamily() {
    return family;
  }

  public void setFamily(int family) {
    this.family = family;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public InetAddress getAddress() {
    return address;
  }

  public void setAddress(InetAddress address) {
    this.address = address;
  }

}
