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

import org.junit.Assert;
import org.junit.Test;

import net.sf.gee.l2mn.stun.message.Message;
import net.sf.gee.l2mn.stun.message.MessageHeader;
import net.sf.gee.l2mn.stun.message.MessageTypeEnum;
import net.sf.gee.l2mn.stun.message.attribute.MessageAttribute;
import net.sf.gee.l2mn.stun.message.attribute.MessageIntegrity;
import net.sf.gee.l2mn.stun.message.attribute.Software;
import net.sf.gee.l2mn.stun.message.attribute.Username;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class MessageAttributeTest {

  @Test
  public void testMessageIntegrity() {

    Message msg = new Message(new MessageHeader(MessageTypeEnum.BINDINGREQUEST));

    msg.getHeader().setTransactionId("TEST");
    msg.getHeader().setMagicCookie(1000);
    msg.getHeader().setLength(0);

    msg.addAttribute(new Software());

    // long term
    final MessageAttribute attr =
        new MessageIntegrity("username", "password", "localhost.localdomain", msg);

    Assert.assertNotEquals(attr.getLength(), 0);
    Assert.assertNotEquals(attr.getType(), 0);
    Assert.assertNotNull(attr.getValue());
  }

  @Test
  public void testUsername() {
    try {
      final MessageAttribute attr = new Username("giorgio");

      Assert.assertNotEquals(attr.getLength(), 0);
      Assert.assertNotEquals(attr.getType(), 0);
      Assert.assertNotNull(attr.getValue());
    }
    catch (IOException e) {
      e.printStackTrace();

      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void testSoftware() {
    final MessageAttribute attr = new Software();

    Assert.assertNotEquals(attr.getLength(), 0);
    Assert.assertNotEquals(attr.getType(), 0);
    Assert.assertNotNull(attr.getValue());
  }

}
