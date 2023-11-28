import io.temporal.workflow.Workflow
import io.temporal.workflow.WorkflowInterface
import java.time.Duration
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SampleKotlinWorkflowImpl : SampleKotlinWorkflow {

    private val log: Logger = LoggerFactory.getLogger(SampleKotlinWorkflowImpl::class.java)

    override fun kotlinWorkflow(sleepSeconds: Long): String {
        log.info("Start Sleeping")
        Workflow.sleep(Duration.ofSeconds(sleepSeconds))
        log.info("Finish Sleeping")

        return "SUCCESS"
    }
}
