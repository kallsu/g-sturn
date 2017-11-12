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

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;

import net.sf.gee.common.util.security.SecurityUtil;
import net.sf.gee.l2mn.stun.message.Message;
import net.sf.gee.l2mn.stun.message.MessageHeader;
import net.sf.gee.l2mn.stun.message.MessageTypeEnum;
import net.sf.gee.l2mn.stun.message.attribute.MessageAttribute;
import net.sf.gee.l2mn.stun.message.attribute.MessageAttributeType;
import net.sf.gee.l2mn.stun.message.attribute.XorMappedAddress;
import net.sf.gee.l2mn.stun.transport.UDPStunTransporter;
import net.sf.gee.l2mn.stun.util.StunUtil;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class UDPStunTransportTest {


  @Test
  public void test() {

    String transactionId = SecurityUtil.generateRandomString(12);
    long magicCookie = (0x2112A442);

    Message request = new Message(new MessageHeader(MessageTypeEnum.BINDINGREQUEST));

    request.getHeader().setTransactionId(transactionId);
    request.getHeader().setMagicCookie(magicCookie);
    request.getHeader().setLength(0);

    try (UDPStunTransporter st = new UDPStunTransporter("stun4.l.google.com", 19305);) {
      // execute bind
      Message response = st.doBind(request);

      Assert.assertNotNull(response);
      Assert.assertNotNull(response.getHeader());
      Assert.assertFalse(response.getAttributes().isEmpty());

      Assert.assertEquals(magicCookie, response.getHeader().getMagicCookie());
      Assert.assertEquals(transactionId, response.getHeader().getTransactionId());

      for (MessageAttribute current : response.getAttributes()) {

        if (current.getType() == MessageAttributeType.XOR_MAPPED_ADDRESS.getCode()) {

          XorMappedAddress obj = (XorMappedAddress) current;

          InetAddress addr = obj.getAddress(StunUtil.longToFourBytes(magicCookie));
          int port = obj.getPort(StunUtil.longToFourBytes(magicCookie));

          Assert.assertNotNull(addr);
          Assert.assertNotEquals(port, -1);

          System.out.printf("%s:%d", addr.toString(), port);
        }
      }

    }
    catch (Exception e) {
      e.printStackTrace();

      Assert.fail(e.getMessage());
    }

  }

}
