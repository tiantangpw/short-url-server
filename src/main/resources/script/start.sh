#!/bin/sh
set -xe

SRV_NAME="short-url"
PRJ_VER="0.0.1-SNAPSHOT"
LOG_HOME="./logs"

# JAVA VAR
JAVA_OPTS=" -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/logs/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/java_pid%p.hprof "
JAVA_MEM_OPTS=" -server -Xms512m -Xmx1g -XX:NewRatio=3 -Xss8m -Djava:security:egd=file:/dev/:/urandom -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap "
JAVA_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
echo "start " $SRV_NAME
nohup java $JAVA_MEM_OPTS $JAVA_OPTS $JAVA_DEBUG_OPTS -jar "$SRV_NAME-$PRJ_VER.jar" --spring.config.location=file:./application.yml 2>&1 >> $LOG_HOME/catalina.log &
echo "done"
