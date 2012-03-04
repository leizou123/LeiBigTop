BASE_DIR=`pwd`
ESP_CLIENT_JAR=$BASE_DIR/target/LeiBigTop-1.1.jar
ESP_CLIENT_CLASSPATH=$ESP_CLIENT_JAR
for jar in `ls /usr/local/groovy-1.8.6/lib/*.jar`; do
  ESP_CLIENT_CLASSPATH=$ESP_CLIENT_CLASSPATH":"$jar
done

ESP_CLIENT_CLASSPATH=$ESP_CLIENT_CLASSPATH":./lib/itest-common-0.3.0-incubating-SNAPSHOT.jar"
echo $ESP_CLIENT_CLASSPATH
echo ""
echo $ESP_CLIENT_JAR

java -cp $ESP_CLIENT_CLASSPATH com.lei.bigtop.hadoop.test.RunHadoopTestFromPropFile

