package com.github.brosgarden.wordcount;

import com.github.brosgarden.grouping.custom.CustomGroupingTopology;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCountTopology {
  private static final Logger logger = LoggerFactory.getLogger(CustomGroupingTopology.class);

  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("line-reader", new LineReaderSpout());
    builder.setBolt("line-split", new LineSplitBolt(), 2).shuffleGrouping("line-reader");
    builder
        .setBolt("word-counter", new WordCounterBolt(), 2)
        .fieldsGrouping("line-split", new Fields("word"));

    Config config = new Config();
    config.setDebug(true);
    config.put("fileToRead", "/Users/rez/storm-example/data.txt");
    config.put("dirToWrite", "/Users/rez/storm-example/");

    LocalCluster cluster = new LocalCluster();
    try {
      cluster.submitTopology("word-count-topology", config, builder.createTopology());
      Thread.sleep(100000);
    } catch (Exception e) {
      logger.error("Issue running topology", e);
    } finally {
      cluster.shutdown();
    }
  }
}
