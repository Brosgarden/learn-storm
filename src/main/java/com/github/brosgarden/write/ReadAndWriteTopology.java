package com.github.brosgarden.write;

import com.github.brosgarden.read.ReadFieldsSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadAndWriteTopology {
  private static final Logger logger = LoggerFactory.getLogger(ReadAndWriteTopology.class);

  public static void main(String[] args) {
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("read-fields-spout", new ReadFieldsSpout());
    builder.setBolt("filter-and-write-bolt", new FilterAndWriteFieldsBolt()).shuffleGrouping("read-fields-spout");

    Config config = new Config();
    config.setDebug(true);
    config.put("fileToRead", "/Users/rez/fileToRead.txt");
    config.put("dirToWrite", "/Users/rez/");

    LocalCluster cluster = new LocalCluster();
    try {
      cluster.submitTopology("read-write-fields-topology", config, builder.createTopology());

      Thread.sleep(10000);
    } catch (Exception e) {
      logger.error("Exception running topology", e);
    } finally{
      cluster.shutdown();
    }
  }
}
