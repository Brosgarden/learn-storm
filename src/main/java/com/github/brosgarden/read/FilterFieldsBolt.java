package com.github.brosgarden.read;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class FilterFieldsBolt extends BaseBasicBolt {

  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    String firstName = input.getStringByField("firstName");
    String lastName = input.getStringByField("lastName");
    collector.emit(new Values(firstName, lastName));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("firstName", "lastName"));
  }
}
