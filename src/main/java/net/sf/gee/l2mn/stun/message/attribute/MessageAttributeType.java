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

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public enum MessageAttributeType {

  // https://tools.ietf.org/html/rfc5389#section-18.2
  //
  // Comprehension-required range (0x0000-0x7FFF):
  // 0x0000: (Reserved)
  // 0x0001: MAPPED-ADDRESS
  // 0x0002: (Reserved; was RESPONSE-ADDRESS)
  // 0x0003: (Reserved; was CHANGE-ADDRESS)
  // 0x0004: (Reserved; was SOURCE-ADDRESS)
  // 0x0005: (Reserved; was CHANGED-ADDRESS)
  // 0x0006: USERNAME
  // 0x0007: (Reserved; was PASSWORD)
  // 0x0008: MESSAGE-INTEGRITY
  // 0x0009: ERROR-CODE
  // 0x000A: UNKNOWN-ATTRIBUTES
  // 0x000B: (Reserved; was REFLECTED-FROM)
  // 0x0014: REALM
  // 0x0015: NONCE
  // 0x0020: XOR-MAPPED-ADDRESS
  //
  // Comprehension-optional range (0x8000-0xFFFF)
  // 0x8022: SOFTWARE
  // 0x8023: ALTERNATE-SERVER
  // 0x8028: FINGERPRINT

  MAPPED_ADDRESS(0x0001), RESPONSE_ADDRES(0x0002), CHANGE_ADDRES(0x0003), SOURCE_ADDRES(
      0x0004), CHANGED_ADDRES(0x0005),

  USERNAME(0x0006), PASSWORD(0x0007),

  MESSAGE_INTEGRITY(0x0008), ERROR_CODE(0x0009),

  UNKNOWN_ATTRIBUTE(0x000A), REFLECTED_FROM(0x000B),

  REALM(0x0014), NONCE(0x0015), XOR_MAPPED_ADDRESS(0x0020),

  SOFTWARE(0x8022), ALTERNATE_SERVER(0x8023), FINGERPRINT(0x8028),

  ;

  private int code = -1;

  /**
   * @param code
   */
  private MessageAttributeType(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  /**
   * Return type enumeration value according the integer type. If integer type not match, it returns
   * null.
   * 
   * @param type integer type
   * 
   * @return {@link MessageAttributeType}
   */
  public static MessageAttributeType getAttributeType(int type) {

    for (MessageAttributeType current : MessageAttributeType.values()) {
      if (current.getCode() == type) {
        return current;
      }
    }

    return null;
  }


}
