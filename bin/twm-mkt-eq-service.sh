APP_NAME="twm-mkt-eq-service"
APP_VERSION="1.0.0-SNAPSHOT"
JAVA_PARAM="-Xmx101m"

BIN_PATH=$PROM_HOME_PARENT/TWM/$APP_NAME/bin     #PROM-HOME-PARENT :: exported in .bashrc
JAR_PATH=$BIN_PATH/../target/quarkus-app/quarkus-run.jar

APP_PARAMS="-Dequity.holdingsFileLocation=${BIN_PATH}/../landing/holdings.csv"

echo "Starting '$APP_NAME' with java param: '$JAVA_PARAM', app params: '$APP_PARAMS' at '$JAR_PATH'"
java $JAVA_PARAM $APP_PARAMS -jar $JAR_PATH
