set APP_NAME="twm-mkt-eq-service"
set APP_VERSION="1.0.0-SNAPSHOT"
set JAVA_PARAM="-Xmx101m"

set BIN_PATH="%TWM_HOME_PARENT%/TWM/%APP_NAME%/bin"
set JAR_PATH="%BIN_PATH%/../target/quarkus-app/quarkus-run.jar"

set APP_PARAMS="-Dequity.holdingsFileLocation=%BIN_PATH%/../landing/holdings.csv -Dequity.executionIntervalDuration=$1"

echo "Starting %APP_NAME% with java param: %JAVA_PARAM%, app params: %APP_PARAMS% at %JAR_PATH%"
java %JAVA_PARAM% -Dequity.holdingsFileLocation=%BIN_PATH%/../landing/holdings.csv -Dequity.executionIntervalDuration=%1 -jar %JAR_PATH%
