FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/client-consumer-iot-0.1.0-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENV TZ UTC
ENTRYPOINT [ "java", \
  "-XX:+UseG1GC", \
  "-XX:MaxRAM=1G", \
  "-Xmx512m", \
  "-XX:MinHeapFreeRatio=20", \
  "-XX:MaxHeapFreeRatio=40", \
  "-XX:MaxGCPauseMillis=200", \
  "-XX:G1HeapRegionSize=8m", \
  "-jar", "/app/app.jar" ]