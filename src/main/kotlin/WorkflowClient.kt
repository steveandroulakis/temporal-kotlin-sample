import io.temporal.client.WorkflowClient as TemporalWorkflowClient
import io.temporal.client.WorkflowOptions
import java.io.FileNotFoundException
import javax.net.ssl.SSLException
import kotlin.random.Random

object WorkflowClient {

    @Throws(FileNotFoundException::class, SSLException::class)
    fun runWorkflow(sleepSeconds: Long): String {
        val workflowId = generateWorkflowId()

        val client = TemporalClient.get()
        val taskQueue = ServerInfo.getTaskqueue()

        val options = WorkflowOptions.newBuilder()
            .setWorkflowId(workflowId)
            .setTaskQueue(taskQueue)
            .build()
        val sampleWorkflow = client.newWorkflowStub(SampleKotlinWorkflow::class.java, options)

        TemporalWorkflowClient.start(sampleWorkflow::kotlinWorkflow, 3)
        println("Running workflow: $workflowId\n\n")

        return workflowId
    }

    // generate a unique workflow ID
    private fun generateWorkflowId(): String {
        val randomChars = (1..3)
            .map { Random.nextInt('A'.toInt(), 'Z'.toInt() + 1).toChar() }
            .joinToString("")
        val randomNumber = Random.nextInt(999)
        return "WORKFLOW-$randomChars-$randomNumber"
    }
}
