package com.github.brosgarden.wordcount;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class WordCounterBolt extends BaseBasicBolt {
  private static final Logger logger = LoggerFactory.getLogger(WordCounterBolt.class);
  private int id;
  private String name;
  private Map<String, Integer> counters;
  private String fileName;

  @Override
  public void prepare(Map stormConf, TopologyContext context) {
    this.counters = new HashMap<>();
    this.name = context.getThisComponentId();
    this.id = context.getThisTaskId();
    this.fileName =
        String.format("%s-output-%s-%s.txt", stormConf.get("dirToWrite").toString(), id, name);
  }

  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    String word = input.getString(0);

    if (counters.containsKey(word)) {
      int i = counters.get(word) + 1;
      counters.put(word, i);
    } else {
      counters.put(word, 1);
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {}

  @Override
  public void cleanup() {
    try {
      PrintWriter writer = new PrintWriter(fileName, "UTF-8");
      BiConsumer<String, Integer> counterConsumer =
          (key, value) -> writer.println(key + ": " + value);
      counters.forEach(counterConsumer);
      writer.flush();
      writer.close();
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      logger.error("Unable to finish cleanup", e);
    }
  }
}
