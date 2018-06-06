package com.github.brosgarden.grouping.shuffle;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShuffleTopology {
  private static final Logger logger = LoggerFactory.getLogger(ShuffleTopology.class);

  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("int-spout", new IntegerSpout());
    builder.setBolt("write-bolt", new WriteToFileBolt(), 2).shuffleGrouping("int-spout");

    Config config = new Config();
    config.setDebug(true);
    config.put("dirToWrite", "/Users/rez/storm-example/");

    LocalCluster cluster = new LocalCluster();

    try {
      cluster.submitTopology("shuffle-topology", config, builder.createTopology());
      Thread.sleep(10000);
    } catch (Exception e) {
      logger.error("Issue running topology", e);
    } finally {
      cluster.shutdown();
    }
  }
}
