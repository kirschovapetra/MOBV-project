package sk.stuba.fei.uim.mobv_project.utils

import org.signal.argon2.Argon2
import org.signal.argon2.MemoryCost
import org.signal.argon2.Type
import org.signal.argon2.Version
import java.security.SecureRandom

class HashingUtils {
    companion object {
        private const val rngAlgorithm = "SHA1PRNG"

        private val argon2 = Argon2.Builder(Version.V13)
                                   .type(Type.Argon2id)
                                   .hashLength(32)
                                   .memoryCost(MemoryCost.MiB(16))
                                   .parallelism(1)
                                   .iterations(3)
                                   .build()

        fun hashString(input: String): String {
            val bytes = ByteArray(16)
            getSecureRandom().nextBytes(bytes)

            return argon2.hash(input.toByteArray(), bytes)
                         .encoded
        }

        fun verifyHash(input: String, hash: String): Boolean {
            return Argon2.verify(hash, input.toByteArray())
        }

        fun getSecureRandom(): SecureRandom {
            return SecureRandom.getInstance(rngAlgorithm)
        }
    }
}