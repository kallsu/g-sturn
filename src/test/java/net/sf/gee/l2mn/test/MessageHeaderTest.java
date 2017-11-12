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
package net.sf.gee.l2mn.test;

import java.io.IOException;

import org.junit.Test;

import net.sf.gee.l2mn.stun.message.MessageHeader;
import net.sf.gee.l2mn.stun.message.MessageTypeEnum;
import net.sf.gee.l2mn.stun.util.StunUtil;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class MessageHeaderTest {

  @Test
  public void testHeader() {
    MessageHeader mh = new MessageHeader(MessageTypeEnum.BINDINGREQUEST);
    mh.setTransactionId("TEST");
    mh.setLength(1024);

    try {
      byte[] type = StunUtil.integerToTwoBytes(mh.getType().getValue());

      for (int i = 0; i < type.length; i++) {
        System.out.println(type[i]);
      }

    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

}
