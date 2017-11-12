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
package net.sf.gee.l2mn.stun.transport;

import java.io.IOException;

import net.sf.gee.l2mn.stun.message.Message;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class TurnTransporter implements Transporter {

  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public TurnTransporter() {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.Closeable#close()
   */
  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see net.sf.gee.l2mn.stun.transport.Transporter#doBind(net.sf.gee.l2mn.stun.message.Message)
   */
  @Override
  public Message doBind(Message request) throws IOException {
    // Requested-transport : UDP

    // Lifetime

    // fingerprint

    return null;
  }

}
