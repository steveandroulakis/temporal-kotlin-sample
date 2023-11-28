object ServerInfo {

    fun getCertPath(): String = System.getenv("TEMPORAL_CERT_PATH") ?: ""

    fun getKeyPath(): String = System.getenv("TEMPORAL_KEY_PATH") ?: ""

    fun getNamespace(): String = System.getenv("TEMPORAL_NAMESPACE") ?: "default"

    fun getAddress(): String = System.getenv("TEMPORAL_ADDRESS") ?: "localhost:7233"

    fun getTaskqueue(): String = System.getenv("TEMPORAL_KOTLIN_TASKQUEUE") ?: "defaultKotlinSample"

    fun getServerInfo(): Map<String, String> = mapOf(
        "certPath" to getCertPath(),
        "keyPath" to getKeyPath(),
        "namespace" to getNamespace(),
        "address" to getAddress(),
        "taskQueue" to getTaskqueue()
    )
}