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
import java.nio.charset.StandardCharsets;

import net.sf.gee.common.util.string.StringUtil;
import net.sf.gee.l2mn.stun.util.StunUtil;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class Username extends MessageAttribute {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public Username(String username) throws IOException {
    super(MessageAttributeType.USERNAME.getCode());

    if (StringUtil.isEmpty(username)) {
      throw new IOException("Username is null or empty");
    }

    // calculate
    byte[] realValue = StunUtil.saslPrep(username).getBytes(StandardCharsets.UTF_8);

    // only success case
    if (realValue.length < 514) {
      setValue(realValue);
      setLength(realValue.length);

    }
    else {
      throw new IOException("Username too long ( 513 byte in UTF-8 )");
    }
  }

}
