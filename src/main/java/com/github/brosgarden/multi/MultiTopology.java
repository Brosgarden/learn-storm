package com.github.brosgarden.multi;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiTopology {
  private static final Logger logger = LoggerFactory.getLogger(MultiTopology.class);

  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("file-reader-spout", new FileReaderSpout());
    builder.setBolt("append-bolt", new AppendBolt()).shuffleGrouping("file-reader-spout");

    Config config = new Config();
    config.setDebug(true);
    config.put("fileToRead", "/Users/rez/fileToRead.txt");

    LocalCluster cluster = new LocalCluster();
    try {
      cluster.submitTopology("file-reader-topology", config, builder.createTopology());

      Thread.sleep(10000);
    } catch (Exception e) {
      logger.error("Exception running topology", e);
    } finally{
      cluster.shutdown();
    }
  }
}
