package com.github.brosgarden.even;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvenTopology {

  private static final Logger logger = LoggerFactory.getLogger(EvenTopology.class);

  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("integer-spout", new IntegerSpout());
    builder.setBolt("multiply-bolt", new MultiplyBolt()).shuffleGrouping("integer-spout");

    Config config = new Config();
    config.setDebug(true);

    LocalCluster cluster = new LocalCluster();

    try {
      cluster.submitTopology("top", config, builder.createTopology());

      Thread.sleep(1000);
    } catch (Exception e) {
      logger.error("Exception running topology", e);
    } finally{
      cluster.shutdown();
    }
  }
}
