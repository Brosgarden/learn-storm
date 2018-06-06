package com.github.brosgarden.grouping.shuffle;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class IntegerSpout extends BaseRichSpout {
  private SpoutOutputCollector collector;
  private int integer = 0;

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector = collector;
  }

  @Override
  public void nextTuple() {
    while (integer < 100) {
      int bucket = integer / 10;
      collector.emit(new Values(integer, bucket));
      integer++;
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("integer", "bucket"));
  }
}
