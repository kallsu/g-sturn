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
package net.sf.gee.l2mn.stun.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.ibm.icu.text.StringPrep;
import com.ibm.icu.text.StringPrepParseException;

import net.sf.gee.logger.factory.GLogFactory;
import net.sf.gee.logger.log.SimpleGLogger;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public final class StunUtil implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  private StunUtil() {
    super();
  }

  /**
   * @param value
   * @return
   * @throws IOException
   */
  public static final int oneByteToInteger(byte value) {
    return value & 0xFF;
  }

  /**
   * @param value
   * @return
   * @throws IOException
   */
  public static final int twoBytesToInteger(byte[] value) throws IOException {
    if (value.length < 2) {
      throw new IOException("Byte array too short!");
    }

    int temp0 = value[0] & 0xFF;
    int temp1 = value[1] & 0xFF;

    return ((temp0 << 8) + temp1);
  }

  /**
   * @param value
   * @return
   * @throws IOException
   */
  public static final long fourBytesToLong(byte[] value) throws IOException {
    if (value.length < 4) {
      throw new IOException("Byte array too short!");
    }
    int temp0 = value[0] & 0xFF;
    int temp1 = value[1] & 0xFF;
    int temp2 = value[2] & 0xFF;
    int temp3 = value[3] & 0xFF;

    return (((long) temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
  }

  /**
   * @param array
   * @param start
   * @param end
   * @return
   */
  public static final byte[] subArray(byte[] array, int start, int end) {

    // error case
    if (array == null || array.length == 0) {
      return new byte[] {};
    }

    if (end - start <= 0) {
      return new byte[] {};
    }

    // output stream
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {

      // iterate
      for (int i = start; i <= end; i++) {
        out.write(array[i]);
      }

      // return
      return out.toByteArray();

    }
    catch (Exception e) {
      return new byte[] {};
    }
  }

  /**
   * @param value
   * @return
   * @throws IOException
   */
  public static final byte integerToOneByte(int value) throws IOException {
    if ((value > Math.pow(2, 15)) || (value < 0)) {
      throw new IOException("Integer value " + value + " is larger than 2^15");
    }

    return (byte) (value & 0xFF);
  }

  /**
   * @param value
   * @return
   * @throws IOException
   */
  public static final byte[] integerToTwoBytes(int value) throws IOException {
    byte[] result = new byte[2];
    if ((value > Math.pow(2, 31)) || (value < 0)) {
      throw new IOException("Integer value " + value + " is larger than 2^31");
    }
    result[0] = (byte) ((value >>> 8) & 0xFF);
    result[1] = (byte) (value & 0xFF);
    return result;
  }

  /**
   * @param value
   * @return
   * @throws IOException
   */
  public static final byte[] longToFourBytes(long value) throws IOException {
    byte[] result = new byte[4];

    if ((value > Math.pow(2, 63)) || (value < 0)) {
      throw new IOException("Integer value " + value + " is larger than 2^63");
    }

    result[0] = (byte) ((value >>> 24) & 0xFF);
    result[1] = (byte) ((value >>> 16) & 0xFF);
    result[2] = (byte) ((value >>> 8) & 0xFF);
    result[3] = (byte) (value & 0xFF);

    return result;
  }

  /**
   * @param source
   * @return
   */
  public static final String saslPrep(String source) {

    final SimpleGLogger LOGGER =
        GLogFactory.getInstance().getLogger(SimpleGLogger.class, StunUtil.class);

    try {
      return StringPrep.getInstance(StringPrep.RFC4013_SASLPREP).prepare(source,
          StringPrep.DEFAULT);
    }
    catch (StringPrepParseException e) {
      LOGGER.logError(e);
    }

    return null;
  }

}
