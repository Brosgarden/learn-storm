package com.github.brosgarden.grouping.all;

import com.github.brosgarden.grouping.shuffle.IntegerSpout;
import com.github.brosgarden.grouping.shuffle.WriteToFileBolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllTopology {
  private static final Logger logger = LoggerFactory.getLogger(AllTopology.class);

  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("int-spout", new IntegerSpout());
    builder.setBolt("write-bolt", new WriteToFileBolt(), 2).allGrouping("int-spout");

    Config config = new Config();
    config.setDebug(true);
    config.put("dirToWrite", "/Users/rez/storm-example/");

    LocalCluster cluster = new LocalCluster();
    try {
      cluster.submitTopology("all-topology", config, builder.createTopology());
      Thread.sleep(10000);
    } catch (Exception e) {
      logger.error("Issue running topology", e);
    } finally {
      cluster.shutdown();
    }
  }
}
