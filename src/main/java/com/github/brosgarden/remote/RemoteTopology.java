package com.github.brosgarden.remote;

import com.github.brosgarden.even.IntegerSpout;
import com.github.brosgarden.even.MultiplyBolt;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;

public class RemoteTopology {

  public static void main(String[] args) throws Exception {
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("first-remote-spout", new IntegerSpout());
    builder.setBolt("first-remote-bolt", new MultiplyBolt()).shuffleGrouping("first-remote-spout");

    Config config = new Config();
    config.setDebug(true);

    StormSubmitter.submitTopology("first-remote-topology", config, builder.createTopology());
  }
}
