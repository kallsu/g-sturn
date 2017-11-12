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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.sf.gee.l2mn.stun.util.StunUtil;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 *         https://tools.ietf.org/html/rfc5389#page-33
 * 
 *         <pre>
 *         {@code
 *  
 *   The format of the XOR-MAPPED-ADDRESS is:
 *
 *      0                   1                   2                   3
 *      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *     |x x x x x x x x|    Family     |         X-Port                |
 *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *     |                X-Address (Variable)
 *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *             Figure 6: Format of XOR-MAPPED-ADDRESS Attribute
 *
 *   The Family represents the IP address family, and is encoded
 *   identically to the Family in MAPPED-ADDRESS.
 * }
 *         </pre>
 *
 */
public class XorMappedAddress extends MessageAttribute {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private byte family = 0x0;

  private byte[] xorPort = null;

  private byte[] xorAddress = null;

  /**
   * 
   */
  public XorMappedAddress() {
    super(MessageAttributeType.XOR_MAPPED_ADDRESS.getCode());
  }

  public XorMappedAddress(int attrLength, byte[] data) {
    this();

    setLength(attrLength);
    setValue(data);

    // restricted
    // StunUtil.oneByteToInteger(data[])

    this.family = data[1];

    this.xorPort = new byte[] {data[2], data[3]};

    this.xorAddress = StunUtil.subArray(data, 4, (attrLength - 1));
  }

  private byte[] xorWithKey(byte[] a, byte[] key) {
    byte[] out = new byte[a.length];

    for (int i = 0; i < a.length; i++) {
      out[i] = (byte) (a[i] ^ key[i % key.length]);
    }

    return out;
  }

  public int getPort(byte[] key) {
    // XOR
    byte[] output = xorWithKey(this.xorPort, key);

    if (output.length != 2) {
      return -1;
    }

    try {
      return StunUtil.twoBytesToInteger(output);
    }
    catch (IOException e) {
      e.printStackTrace();

      return -1;
    }
  }

  public InetAddress getAddress(byte[] key) throws UnknownHostException {

    // IPv4
    if (StunUtil.oneByteToInteger(family) == 1) {

      // XOR
      byte[] output = xorWithKey(xorAddress, key);

      return Inet4Address.getByAddress(output);

    } // IPv6
    else {

      // XOR
      byte[] output = xorWithKey(xorAddress, key);

      return Inet6Address.getByAddress(output);
    }
  }

  public byte getFamily() {
    return family;
  }

  public byte[] getXorPort() {
    return xorPort;
  }

  public byte[] getXorAddress() {
    return xorAddress;
  }

  public void setFamily(byte family) {
    this.family = family;
  }

  public void setXorPort(byte[] xorPort) {
    this.xorPort = xorPort;
  }

  public void setXorAddress(byte[] xorAddress) {
    this.xorAddress = xorAddress;
  }



}
