FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/client-consumer-iot-0.1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENV TZ UTC

# OpenTelemetry
ENV OTEL_SERVICE_NAME "VOIS IOT CONSUMER EVENTS"
ENV OTEL_RESOURCE_ATTRIBUTES "deployment.environment=production,service.namespace=com.vodafone.vois.iotlabs,service.version=v1.1"
ENV OTEL_RESOURCE_PROVIDERS_AWS_ENABLED=true
ENV JAVA_TOOL_OPTIONS=-javaagent:/app/grafana-opentelemetry-java.jar
#ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
#ENV OTEL_EXPORTER_OTLP_PROTOCOL=grpc

ENTRYPOINT [ "java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAM=1G", \
  "-Xmx512m", \
  "-XX:MinHeapFreeRatio=20", \
  "-XX:MaxHeapFreeRatio=40", \
  "-XX:MaxGCPauseMillis=200", \
  "-XX:G1HeapRegionSize=8m", \
  "-jar", "/app/app.jar" ]