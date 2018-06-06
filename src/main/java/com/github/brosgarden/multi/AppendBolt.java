package com.github.brosgarden.multi;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class AppendBolt extends BaseBasicBolt {

  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    collector.emit(new Values(input.getString(0) + " through bolt"));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("word-appended"));
  }
}
