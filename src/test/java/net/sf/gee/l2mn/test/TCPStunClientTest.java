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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.junit.Assert;
import org.junit.Test;

import net.sf.gee.common.util.numeric.NumericUtil;
import net.sf.gee.l2mn.stun.message.Message;
import net.sf.gee.l2mn.stun.message.MessageHeader;
import net.sf.gee.l2mn.stun.message.MessageTypeEnum;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class TCPStunClientTest {


  @Test
  public void test() {

    int transactionId = NumericUtil.generateRandom(1, Integer.MAX_VALUE);
    int magicCookie = transactionId * (0x2112A442);

    try {

      Message msg = new Message(new MessageHeader(MessageTypeEnum.BINDINGREQUEST));

      msg.getHeader().setTransactionId(String.valueOf(transactionId));
      msg.getHeader().setMagicCookie(magicCookie);
      msg.getHeader().setLength(0);

      InetSocketAddress hostAddress = new InetSocketAddress("stun3.l.google.com", 19305);
      DatagramSocket client = new DatagramSocket();
      client.connect(hostAddress);

      byte[] data = msg.getHeader().getBytes();
      DatagramPacket dp = new DatagramPacket(data, data.length);

      client.send(dp);

      byte[] receiveData = new byte[1024];
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      client.receive(receivePacket);

      client.close();
    }
    catch (Exception e) {
      e.printStackTrace();

      Assert.fail(e.getMessage());
    }

  }

}
