package sk.stuba.fei.uim.mobv_project.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HashingUtilsTest {

    private val input = "asdf1234"

    @Test
    fun hashString() {
        val output = HashingUtils.hashString(input)

        assertTrue(output.startsWith("\$argon2"))
    }

    @Test
    fun verifyHash() {
        val hash =
            "\$argon2id\$v=19\$m=32768,t=3,p=1\$JCRInF1dqcUclccyshyaoA\$SeAO//kO27epT2u7z31ImFlw6GZZ8XbjKFt4fpq/ukY"
        val hash2 =
            "\$argon2id\$v=19\$m=32768,t=3,p=1\$KGR9uQkswNw/M/CKavyCfQ\$fAe4Jbi8JOGcn0KwSXR3UPm2ghWVpeITn+XU/j1WbQU"
        val invalidHash =
            "\$argon2id\$v=19\$m=32768,t=3,p=1\$asdfasdfasdf"
        val invalidHash2 =
            "\$argon2id\$v=19\$m=32768,t=3,p=1\$KGR9uQkswNw/M/CKavyCfQ\$fAe4Jbi8asdfasdfasdfasdfghWVpeITn+XU/j1WbQU"

        assertTrue(HashingUtils.verifyHash(input, hash))
        assertTrue(HashingUtils.verifyHash(input, hash2))

        assertFalse(HashingUtils.verifyHash(input, invalidHash))
        assertFalse(HashingUtils.verifyHash(input, invalidHash2))
    }

}