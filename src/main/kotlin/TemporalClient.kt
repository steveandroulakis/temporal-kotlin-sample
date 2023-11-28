import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.converter.CodecDataConverter
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.serviceclient.SimpleSslContextBuilder
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.net.ssl.SSLException

object TemporalClient {

    @Throws(FileNotFoundException::class, SSLException::class)
    fun getWorkflowServiceStubs(): WorkflowServiceStubs {
        val workflowServiceStubsOptionsBuilder = WorkflowServiceStubsOptions.newBuilder()

        if (ServerInfo.getCertPath().isNotEmpty() && ServerInfo.getKeyPath().isNotEmpty()) {
            val clientCert = FileInputStream(ServerInfo.getCertPath())
            val clientKey = FileInputStream(ServerInfo.getKeyPath())

            workflowServiceStubsOptionsBuilder.setSslContext(
                SimpleSslContextBuilder.forPKCS8(clientCert, clientKey).build()
            )
        }

        val targetEndpoint = ServerInfo.getAddress()
        workflowServiceStubsOptionsBuilder.setTarget(targetEndpoint)
        return if (ServerInfo.getAddress() != "localhost:7233") {
            WorkflowServiceStubs.newServiceStubs(workflowServiceStubsOptionsBuilder.build())
        } else {
            WorkflowServiceStubs.newLocalServiceStubs()
        }
    }

    @Throws(FileNotFoundException::class, SSLException::class)
    fun get(): WorkflowClient {
        val service = getWorkflowServiceStubs()

        val builder = WorkflowClientOptions.newBuilder()
        if (System.getenv("ENCRYPT_PAYLOADS") == "true") {
            println("<<<<ENCRYPTING PAYLOADS>>>>")
            builder.setDataConverter(
                CodecDataConverter(
                    DefaultDataConverter.newDefaultInstance(),
                    listOf(CryptCodec()),
                    true // encode failure attributes
                )
            )
        }

        println("<<<<SERVER INFO>>>>:\n ${ServerInfo.getServerInfo()}")
        val clientOptions = builder.setNamespace(ServerInfo.getNamespace()).build()
        return WorkflowClient.newInstance(service, clientOptions)
    }

}
