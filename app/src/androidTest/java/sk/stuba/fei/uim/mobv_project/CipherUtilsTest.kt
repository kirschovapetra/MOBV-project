package sk.stuba.fei.uim.mobv_project

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sk.stuba.fei.uim.mobv_project.utils.CipherUtils

@RunWith(AndroidJUnit4::class)
class CipherUtilsTest {

    @Test
    fun testEncryptDecrypt() {
        val input = "testovací input s ľubozvučnou diakritikou"
        val password = "password"
        val salt = CipherUtils.getSalt()
        val iv = CipherUtils.getIv()

        Log.i("input", input)

        val encrypted = CipherUtils.encrypt(
            input,
            password,
            salt,
            iv
        )

        Log.i("encrypted", encrypted)

        val decrypted = CipherUtils.decrypt(
            encrypted,
            password,
            salt,
            iv
        )

        Log.i("decrypted", decrypted)

        assert(input == decrypted)
    }

}