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

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.sf.gee.l2mn.stun.message.Message;
import net.sf.gee.l2mn.stun.util.StunUtil;
import net.sf.gee.logger.factory.GLogFactory;
import net.sf.gee.logger.log.SimpleGLogger;

/**
 * https://tools.ietf.org/html/rfc5389#section-15.4
 * 
 * The key for the HMAC depends on whether long-term or short-term credentials are in use. For
 * long-term credentials, the key is 16 bytes:
 * 
 * key = MD5(username ":" realm ":" SASLprep(password))
 * 
 * That is, the 16-byte key is formed by taking the MD5 hash of the result of concatenating the
 * following five fields: (1) the username, with any quotes and trailing nulls removed, as taken
 * from the USERNAME attribute (in which case SASLprep has already been applied); (2) a single
 * colon; (3) the realm, with any quotes and trailing nulls removed; (4) a single colon; and (5) the
 * password, with any trailing nulls removed and after processing using SASLprep. For example, if
 * the username was 'user', the realm was 'realm', and the password was 'pass', then the 16-byte
 * HMAC key would be the result of performing an MD5 hash on the string 'user:realm:pass', the
 * resulting hash being 0x8493fbc53ba582fb4c044c456bdc40eb.
 * 
 * For short-term credentials:
 * 
 * key = SASLprep(password)
 * 
 * where MD5 is defined in RFC 1321 [RFC1321] and SASLprep() is defined in RFC 4013 [RFC4013].
 * 
 * @author Giorgio Desideri - giorgio.desideri@gmail.com
 *
 */
public class MessageIntegrity extends MessageAttribute {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final SimpleGLogger LOGGER =
      GLogFactory.getInstance().getLogger(SimpleGLogger.class, MessageIntegrity.class);

  private String key = null;

  private String username = null;

  private String realm = null;

  private String password = null;

  /**
   * Constructor with username and password.
   * 
   * Long-term credentials.
   * 
   * @param username
   * @param password
   */
  public MessageIntegrity(String username, String password, String realm, Message message) {
    super(MessageAttributeType.MESSAGE_INTEGRITY.getCode());

    this.username = username;
    this.password = password;
    this.realm = realm;

    initLongTermKey();

    // calculate
    calculate(message);
  }

  private void initLongTermKey() {
    // key = MD5(username ":" realm ":" SASLprep(password))

    String plainTextKey = String.format("%s:%s:%s", username, realm, StunUtil.saslPrep(password));

    try {
      final MessageDigest md5Hash = MessageDigest.getInstance("MD5");
      md5Hash.update(plainTextKey.getBytes(), 0, plainTextKey.length());

      // this is the key
      this.key = new BigInteger(1, md5Hash.digest()).toString(16);

    }
    catch (NoSuchAlgorithmException e) {
      LOGGER.logError(e);
    }
  }

  /**
   * Constructor with password. Short-term credentials
   * 
   * @param password
   */
  public MessageIntegrity(String password, Message message) {
    super(MessageAttributeType.MESSAGE_INTEGRITY.getCode());

    this.password = password;

    // for short term need only SASLPrep(password)
    this.key = StunUtil.saslPrep(password);

    // calculate
    calculate(message);
  }

  /**
   * Calculate the HMAC-SHA1 of the message.
   * 
   * @param message
   */
  protected void calculate(Message message) {

    try (ByteArrayOutputStream bout = new ByteArrayOutputStream();) {

      // iterate on payload of packet
      for (MessageAttribute current : message.getAttributes()) {
        bout.write(current.getValue());
      }

      byte[] data = bout.toByteArray();

      SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");

      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(signingKey);

      super.setValue(mac.doFinal(data));
      super.setLength(super.getValue().length);
    }
    catch (Exception e) {
      LOGGER.logError(e);
    }
  }

  public String getKey() {
    return key;
  }

  public String getUsername() {
    return username;
  }

  public String getRealm() {
    return realm;
  }

  public String getPassword() {
    return password;
  }

}
