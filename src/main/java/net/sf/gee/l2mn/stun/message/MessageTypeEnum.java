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

/**
 * Message type structure : https://tools.ietf.org/html/rfc5389#section-6
 * 
 * <pre>
 * {@code
 *  0                 1
 *  2  3  4 5 6 7 8 9 0 1 2 3 4 5
 *  +--+--+-+-+-+-+-+-+-+-+-+-+-+-+
 *  |M |M |M|M|M|C|M|M|M|C|M|M|M|M|
 *  |11|10|9|8|7|1|6|5|4|0|3|2|1|0|
 *  +--+--+-+-+-+-+-+-+-+-+-+-+-+-+
 * }
 * 
 * 
 * Here the bits in the message type field are shown as most significant (M11) through least
 * significant (M0). M11 through M0 represent a 12- bit encoding of the method. C1 and C0 represent
 * a 2-bit encoding of the class. A class of 0b00 is a request, a class of 0b01 is an indication, a
 * class of 0b10 is a success response, and a class of 0b11 is an error response. This specification
 * defines a single method, Binding. The method and class are orthogonal, so that for each method, a
 * request, success response, error response, and indication are possible for that method.
 * Extensions defining new methods MUST indicate which classes are permitted for that method.
 * 
 * For example, a Binding request has class=0b00 (request) and method=0b000000000001 (Binding) and
 * is encoded into the first 16 bits as 0x0001. A Binding response has class=0b10 (success response)
 * and method=0b000000000001, and is encoded into the first 16 bits as 0x0101.
 * 
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public enum MessageTypeEnum {

  BINDINGREQUEST(0x0001), BINDINGRESPONSE(0x0101), BINDINGERRORRESPONSE(0x0111),

  SHAREDSECRETREQUEST(0x0002), SHAREDSECRETRESPONSE(0x0102), SHAREDSECRETERRORRESPONSE(0x0112),

  ;

  private int value = -1;

  private MessageTypeEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static MessageTypeEnum getValueByValue(int value) {

    for (MessageTypeEnum current : MessageTypeEnum.values()) {
      if (current.getValue() == value) {
        return current;
      }
    }

    return null;
  }

  public boolean isError() {
    if (value == BINDINGERRORRESPONSE.value || value == SHAREDSECRETERRORRESPONSE.value) {
      return true;
    }

    return false;
  }
}
