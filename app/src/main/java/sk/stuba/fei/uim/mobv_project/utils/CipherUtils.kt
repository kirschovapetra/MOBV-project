package sk.stuba.fei.uim.mobv_project.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec

object CipherUtils {

    private const val cipherAlgorithm = "AES"
    private const val cipherAlgorithmMode = "CTR"
    private const val cipherAlgorithmPadding = "PKCS7Padding"
    private const val cipherTransformation = "$cipherAlgorithm/$cipherAlgorithmMode/$cipherAlgorithmPadding"

    private const val secretKeyDerivationAlgorithm = "PBKDF2WithHmacSHA256"
    private const val secureRandomAlgorithm = "SHA1PRNG"

    /**
     * @param content String to be encrypted
     * @param password String to be used for key derivation
     * @param salt get by calling [getSalt]
     * @param iv get by calling [getIv]
     */
    fun encrypt(content: String, password: String, salt: String, iv: String): String {
        return encodeToString(cipher(ENCRYPT_MODE, content.toByteArray(), password, salt, iv))
    }

    /**
     * @param content encrypted [ByteArray] encoded in Base64 format
     * @param password String to be used for key derivation
     * @param salt reuse salt from encrypt process
     * @param iv reuse iv from encrypt process
     */
    fun decrypt(content: String, password: String, salt: String, iv: String): String {
        return String(cipher(DECRYPT_MODE, decodeToByteArray(content), password, salt, iv))
    }

    /**
     * @return Base64 encoded salt [ByteArray]
     */
    fun getSalt(): String {
        val salt = ByteArray(256)
        getSecureRandom().nextBytes(salt)

        return encodeToString(salt)
    }

    /**
     * @return Base64 encoded iv [ByteArray]
     */
    fun getIv(): String {
        val iv = ByteArray(16)
        getSecureRandom().nextBytes(iv)

        return encodeToString(iv)
    }

    private fun encodeToString(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun decodeToByteArray(base64String: String): ByteArray {
        return Base64.decode(base64String, Base64.DEFAULT)
    }

    private fun cipher(mode: Int, content: ByteArray, password: String, salt: String, iv: String): ByteArray {
        val cipher = Cipher.getInstance(cipherTransformation)
        val saltByteArray = decodeToByteArray(salt)
        val ivParameterSpec = IvParameterSpec(decodeToByteArray(iv))

        val key = getKeyFromPassword(password.toCharArray(), saltByteArray)

        cipher.init(mode, key, ivParameterSpec)

        return cipher.doFinal(content)
    }

    private fun getKeyFromPassword(password: CharArray, salt: ByteArray): SecretKey {
        val pbeKeySpec = PBEKeySpec(password, salt, 1325, 256)
        val factory = SecretKeyFactory.getInstance(secretKeyDerivationAlgorithm)

        return factory.generateSecret(pbeKeySpec)
    }

    private fun getSecureRandom(): SecureRandom {
        return SecureRandom.getInstance(secureRandomAlgorithm)
    }

}