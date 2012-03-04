# rm -rf /tmp/hadoop-lei/dfs
# hadoop namenode -format

#hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.wordcount.CountWordsV2 /Users/lei/work/hadoop-example/input /Users/lei/work/hadoop-example/output

#hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.wordcount.CountWordsV2 /Users/lei/work/hadoop-example/input /Users/lei/work/hadoop-example/output

#ant runWordCounts

#ant -Dmainclass=com.lei.bigtop.hadoop.wordcount.CountWordsV2 -Dinput=/Users/lei/work/hadoop-example/input -Doutput=/Users/lei/work/hadoop-example/output runWordCountsWithArg

#hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.wordcount.CountWordsV2 /Users/lei/work/hadoop-example/input /Users/lei/work/hadoop-example/output

#hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.hadoop.countavg.CountAvg /Users/lei/Documents/workspace/LeiBigTop/data /Users/lei/Documents/workspace/LeiBigTop/output 

BASE_DIR=`pwd`
ESP_CLIENT_JAR=$BASE_DIR/target/release/LeiBigTop-1.1.jar
ESP_CLIENT_CLASSPATH=
for jar in `ls ./lib/*.jar`; do
  ESP_CLIENT_CLASSPATH=$ESP_CLIENT_CLASSPATH":"$BASE_DIR/$jar
done

echo $ESP_CLIENT_CLASSPATH
echo $ESP_CLIENT_JAR

export HADOOP_CLASSPATH=$ESP_CLIENT_CLASSPATH

#hadoop jar $ESP_CLIENT_JAR com.lei.bigtop.hadoop.countavg.CountAvg /Users/lei/Documents/workspace/LeiBigTop/data /Users/lei/Documents/workspace/LeiBigTop/output 


hadoop jar ./target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.calsum.CalSum ./data ./output

hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.calsum.CalSum /Users/lei/Documents/workspace/LeiBigTop/data /Users/lei/Documents/workspace/LeiBigTop/output

groovy -cp ./lib/itest-common-0.3.0-incubating-SNAPSHOT.jar ./src/com/lei/bigtop/hadoop/test/RunHadoopTest.groovy "hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.calsum.CalSum ./data ./output"
