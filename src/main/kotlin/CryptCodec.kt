import com.google.protobuf.ByteString
import io.temporal.api.common.v1.Payload
import io.temporal.common.converter.DataConverterException
import io.temporal.common.converter.EncodingKeys
import io.temporal.payload.codec.PayloadCodec
import io.temporal.payload.codec.PayloadCodecException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptCodec : PayloadCodec {

    companion object {
        val METADATA_ENCODING = ByteString.copyFrom("binary/encrypted", StandardCharsets.UTF_8)

        private const val CIPHER = "AES/GCM/NoPadding"
        const val METADATA_ENCRYPTION_CIPHER_KEY = "encryption-cipher"
        val METADATA_ENCRYPTION_CIPHER = ByteString.copyFrom(CIPHER, StandardCharsets.UTF_8)
        const val METADATA_ENCRYPTION_KEY_ID_KEY = "encryption-key-id"

        private const val GCM_NONCE_LENGTH_BYTE = 12
        private const val GCM_TAG_LENGTH_BIT = 128
        private val UTF_8 = StandardCharsets.UTF_8

        private fun getNonce(size: Int): ByteArray {
            val nonce = ByteArray(size)
            SecureRandom().nextBytes(nonce)
            return nonce
        }
    }

    override fun encode(payloads: List<Payload>): List<Payload> {
        return payloads.map { encodePayload(it) }
    }

    override fun decode(payloads: List<Payload>): List<Payload> {
        return payloads.map { decodePayload(it) }
    }

    private fun encodePayload(payload: Payload): Payload {
        val keyId = getKeyId()
        val key = getKey(keyId)

        val encryptedData = try {
            encrypt(payload.toByteArray(), key)
        } catch (e: Throwable) {
            throw DataConverterException(e)
        }

        return Payload.newBuilder()
            .putMetadata(EncodingKeys.METADATA_ENCODING_KEY, METADATA_ENCODING)
            .putMetadata(METADATA_ENCRYPTION_CIPHER_KEY, METADATA_ENCRYPTION_CIPHER)
            .putMetadata(METADATA_ENCRYPTION_KEY_ID_KEY, ByteString.copyFromUtf8(keyId))
            .setData(ByteString.copyFrom(encryptedData))
            .build()
    }

    private fun decodePayload(payload: Payload): Payload {
        if (METADATA_ENCODING == payload.getMetadataOrDefault(EncodingKeys.METADATA_ENCODING_KEY, null)) {
            val keyId = try {
                payload.getMetadataOrThrow(METADATA_ENCRYPTION_KEY_ID_KEY).toString(UTF_8)
            } catch (e: Exception) {
                throw PayloadCodecException(e)
            }
            val key = getKey(keyId)

            return try {
                val plainData = decrypt(payload.getData().toByteArray(), key)
                Payload.parseFrom(plainData)
            } catch (e: Throwable) {
                throw PayloadCodecException(e)
            }
        } else {
            return payload
        }
    }

    private fun getKeyId(): String {
        // Implementation as per your logic
        return "sa-rocks!sa-rocks!sa-rocks!yeah!"
    }

    private fun getKey(keyId: String): SecretKey {
        // Implementation as per your logic
        return SecretKeySpec(keyId.toByteArray(UTF_8), "AES")
    }

    private fun encrypt(plainData: ByteArray, key: SecretKey): ByteArray {
        val nonce = getNonce(GCM_NONCE_LENGTH_BYTE)
        val cipher = Cipher.getInstance(CIPHER).apply {
            init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH_BIT, nonce))
        }
        val encryptedData = cipher.doFinal(plainData)
        return ByteBuffer.allocate(nonce.size + encryptedData.size)
            .put(nonce)
            .put(encryptedData)
            .array()
    }

    private fun decrypt(encryptedDataWithNonce: ByteArray, key: SecretKey): ByteArray {
        val buffer = ByteBuffer.wrap(encryptedDataWithNonce)
        val nonce = ByteArray(GCM_NONCE_LENGTH_BYTE).apply { buffer.get(this) }
        val encryptedData = ByteArray(buffer.remaining()).apply { buffer.get(this) }
        val cipher = Cipher.getInstance(CIPHER).apply {
            init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH_BIT, nonce))
        }
        return cipher.doFinal(encryptedData)
    }
}
