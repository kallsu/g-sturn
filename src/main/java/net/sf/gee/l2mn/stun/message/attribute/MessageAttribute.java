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

import java.io.Serializable;
import java.util.Arrays;

/**
 * https://tools.ietf.org/html/rfc5389#section-15
 * 
 * {@code
 *  After the STUN header are zero or more attributes.  Each attribute
 *  MUST be TLV encoded, with a 16-bit type, 16-bit length, and value.
 *  Each STUN attribute MUST end on a 32-bit boundary.  As mentioned
 *  above, all fields in an attribute are transmitted most significant
 *  bit first.
 *
 *      0                   1                   2                   3
 *      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *     |         Type                  |            Length             |
 *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *     |                         Value (variable)                ....
 *     +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *
 *                   Figure 4: Format of STUN Attributes
 *
 *  The value in the length field MUST contain the length of the Value
 *  part of the attribute, prior to padding, measured in bytes.  Since
 *  STUN aligns attributes on 32-bit boundaries, attributes whose content
 *  is not a multiple of 4 bytes are padded with 1, 2, or 3 bytes of
 *  padding so that its value contains a multiple of 4 bytes.  The
 *  padding bits are ignored, and may be any value.
 * }
 * 
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public abstract class MessageAttribute implements Serializable {

  private static final long serialVersionUID = 1L;

  private int type = 0;

  private int length = 0;

  private byte[] value = null;

  /**
   * 
   */
  public MessageAttribute() {
    super();
  }

  /**
   * @param type
   */
  public MessageAttribute(int type) {
    this();

    this.type = type;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public byte[] getValue() {
    return value;
  }

  public void setValue(byte[] value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + type;
    result = prime * result + Arrays.hashCode(value);
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
    MessageAttribute other = (MessageAttribute) obj;
    if (type != other.type)
      return false;
    if (!Arrays.equals(value, other.value))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MessageAttribute [type=").append(type).append(", length=").append(length)
        .append(", ");
    if (value != null)
      builder.append("value=").append(Arrays.toString(value));
    builder.append("]");
    return builder.toString();
  }

}
