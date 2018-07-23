This a Gradle project which wraps each [known plugin](settings.gradle) with a common set of tasks
and dependencies.

Please see the example Random Content Connector's [README](random-connector/README.md) for instructions on how to build, deploy and run.

For fusion:

Inside fusion.properties file:

connectors-rpc.jvmOptions = -Xmx1g -Xss256k -Djavax.net.ssl.trustStore=/opt/fusion/4.0.1/apps/jetty/connectors-classic/etc/keystore -Djavax.net.ssl.trustStorePassword=changeit

connectors-classic.jvmOptions = -Xmx1g -Xss256k -Dcom.lucidworks.connectors.pipelines.embedded=false -Djavax.net.ssl.trustStore=/opt/fusion/4.0.1/apps/jetty/connectors-classic/etc/keystore -Djavax.net.ssl.trustStorePassword=changeit

