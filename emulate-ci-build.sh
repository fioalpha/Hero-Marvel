#!/bin/sh

set -ex

./gradlew --stop
./gradlew clean spotlessCheck --no-daemon --stacktrace
./gradlew clean detekt --no-daemon --stacktrace
./gradlew clean test --no-daemon --stacktrace
./gradlew clean assembleDebug -xlint --no-daemon --stacktrace