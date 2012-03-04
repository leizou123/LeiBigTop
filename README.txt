Here is the commands to run the CountWordV2

hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.wordcount.CountWordsV2 /Users/lei/work/hadoop-example/input /Users/lei/work/hadoop-example/output

hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/LeiBigTop-1.1.jar com.lei.bigtop.hadoop.wordcount.CountWordsV2 /Users/lei/work/hadoop-example/input /Users/lei/work/hadoop-example/output

ant runWordCounts

ant -Dmainclass=com.lei.bigtop.hadoop.wordcount.CountWordsV2 -Dinput=/Users/lei/work/hadoop-example/input -Doutput=/Users/lei/work/hadoop-example/output runWordCountsWithArg


