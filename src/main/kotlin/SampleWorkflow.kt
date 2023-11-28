import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface SampleKotlinWorkflow {
    @WorkflowMethod
    fun kotlinWorkflow(input: Long): String
}
