rm /Users/lei/work/hadoop-example/movingaverage/output/*
cp ./lib/json-lib-2.4-jdk15.jar /usr/local//hadoop20/share/hadoop/lib/json-lib-2.4-jdk15.jar
cp ./lib/ezmorph-1.0.6.jar /usr/local//hadoop20/share/hadoop/lib/ezmorph-1.0.6.jar
hadoop fs -rmr /Users/lei/Documents/workspace/LeiBigTop/data
hadoop fs -rmr /Users/lei/Documents/workspace/LeiBigTop/output
hadoop fs -mkdir /Users/lei/Documents/workspace/LeiBigTop/data
hadoop fs -put /Users/lei/Documents/workspace/LeiBigTop/data/* /Users/lei/Documents/workspace/LeiBigTop/data
hadoop jar /Users/lei/Documents/workspace/LeiBigTop/target/release/LeiBigTop-1.1.jar  \
com.lei.bigtop.hadoop.calsum.CalSum \
/Users/lei/Documents/workspace/LeiBigTop/data \
/Users/lei/Documents/workspace/LeiBigTop/output 
hadoop fs -cat /Users/lei/Documents/workspace/LeiBigTop/output/*

