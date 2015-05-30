package utils

import java.security.SecureRandom

/**
 * Created by pnagarjuna on 30/05/15.
 */
object Utils {
  def randomStr: String = BigInt(130, new SecureRandom()).toString(32)
}
