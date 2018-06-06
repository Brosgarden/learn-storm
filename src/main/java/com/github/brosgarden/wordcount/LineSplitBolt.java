package com.github.brosgarden.wordcount;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class LineSplitBolt extends BaseBasicBolt {
  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    String line = input.getStringByField("line");
    String[] words = line.split(" ");
    for (String word : words) {
      word = word.trim();
      if (!word.isEmpty()) {
        collector.emit(new Values(word.toLowerCase()));
      }
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("word"));
  }
}
