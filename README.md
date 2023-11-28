## Temporal Kotlin Sample

## Run Locally

Run a Temporal Server ([Guide](https://docs.temporal.io/kb/all-the-ways-to-run-a-cluster#temporal-cli))
- `brew install temporal`
- `temporal server start-dev` (Temporal Server web UI: localhost:8233)
- `./gradlew run`

This will run one workflow and a worker to execute it.

See the result here: [http://localhost:8233/namespaces/default/workflows](http://localhost:8233/namespaces/default/workflows)

## Run on Temporal Cloud

Run the following command, replacing the variable values with your Temporal Cloud credentials:

```
ENCRYPT_PAYLOADS=false \
TEMPORAL_ADDRESS="TEMPORAL_NAMESPACE.ACCOUNT.tmprl.cloud:7233" \
TEMPORAL_NAMESPACE="TEMPORAL_NAMESPACE.ACCOUNT" \
TEMPORAL_CERT_PATH="/path/to/entity.cert" \
TEMPORAL_KEY_PATH="/path/to/entity.key" \
./gradlew run
```

The resulting workflow will appear on in the Temporal Cloud UI: https://cloud.temporal.io

#### Enable Encryption

Set `ENCRYPT_PAYLOADS` in the command above to "true":

You can decrypt these payloads in Temporal Cloud's UI/cli using the Codec Server: `https://codec.tmprl-demo.cloud` ([source](https://github.com/steveandroulakis/temporal-codec-server)). Codec server configuration can be found on the namespace page (Glasses icon on the top right).

Ensure you switch on "Pass the user access token with your endpoint" setting in the Codec Configuration in Temporal Cloud.

Note: The codec server is *only compatible with workflows running in Temporal Cloud*.