fun main(args: Array<String>) {

    val command = """
        *** INSTRUCTIONS ***
        Example command (leave blank for local Temporal Server):
        
        ENCRYPT_PAYLOADS=false \
        TEMPORAL_ADDRESS="TEMPORAL_NAMESPACE.ACCOUNT.tmprl.cloud:7233" \
        TEMPORAL_NAMESPACE="TEMPORAL_NAMESPACE.ACCOUNT" \
        TEMPORAL_CERT_PATH="/path/to/entity.cert" \
        TEMPORAL_KEY_PATH="/path/to/entity.key" \
        ./gradlew run
        *******************
    """.trimIndent()

    println(command)

    WorkflowClient.runWorkflow(3);

    runWorker();

}