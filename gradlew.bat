@echo off
setlocal
set DIR=%~dp0
set JAR=%DIR%gradle\wrapper\gradle-wrapper.jar
if not exist "%JAR%" (
  echo gradle-wrapper.jar is not bundled in this source archive.
  echo For GitHub builds, use .github\workflows\build.yml; it installs Gradle 8.14 automatically.
  echo For local builds, install Gradle 8.14 and run: gradle clean build
  exit /b 1
)
java -classpath "%JAR%" org.gradle.wrapper.GradleWrapperMain %*
