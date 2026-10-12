#!/bin/sh
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$JAR" ]; then
  echo "gradle-wrapper.jar is not bundled in this source archive."
  echo "For GitHub builds, use the included .github/workflows/build.yml (it installs Gradle 8.14 automatically)."
  echo "For local builds, install Gradle 8.14 and run: gradle clean build"
  exit 1
fi
exec java -classpath "$JAR" org.gradle.wrapper.GradleWrapperMain "$@"
