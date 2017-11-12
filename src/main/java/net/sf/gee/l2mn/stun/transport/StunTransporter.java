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

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

import net.sf.gee.l2mn.stun.message.Message;

/**
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public interface StunTransporter extends Serializable, Closeable {

  /**
   * Execute the STUN bind mechanism
   * 
   * @param request {@link Message}
   * @return the response as {@link Message}
   * @throws IOException
   */
  Message doBind(final Message request) throws IOException;

  /**
   * Execute the STUN shared secret mechanism
   * 
   * @param request {@link Message}
   * @return the response as {@link Message}
   * @throws IOException
   */
  Message doSharedSecret(final Message request) throws IOException;
}
