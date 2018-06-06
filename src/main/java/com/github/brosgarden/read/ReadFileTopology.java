package com.github.brosgarden.read;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadFileTopology {
  private static final Logger logger = LoggerFactory.getLogger(ReadFileTopology.class);

  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("read-fields-spout", new ReadFieldsSpout());
    builder.setBolt("filter-bolt", new FilterFieldsBolt()).shuffleGrouping("read-fields-spout");

    Config config = new Config();
    config.setDebug(true);
    config.put("fileToRead", "/Users/rez/fileToRead.txt");

    LocalCluster cluster = new LocalCluster();
    try {
      cluster.submitTopology("read-fields-topology", config, builder.createTopology());

      Thread.sleep(10000);
    } catch (Exception e) {
      logger.error("Exception running topology", e);
    } finally{
      cluster.shutdown();
    }
  }
}
