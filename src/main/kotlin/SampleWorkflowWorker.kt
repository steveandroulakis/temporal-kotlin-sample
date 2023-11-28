import io.temporal.client.WorkflowOptions
import io.temporal.worker.WorkerFactory
import io.temporal.workflow.WorkflowInterface

fun runWorker() {
    val taskQueue = ServerInfo.getTaskqueue()

    // worker factory that can be used to create workers for specific task queues
    val factory = WorkerFactory.newInstance(TemporalClient.get())
    val worker = factory.newWorker(taskQueue)
    worker.registerWorkflowImplementationTypes(SampleKotlinWorkflowImpl::class.java)
    factory.start()

    println("Worker started for task queue: $taskQueue")

    // Start all workers created by this factory.
    factory.start()
}