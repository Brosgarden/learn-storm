package com.github.brosgarden.grouping.shuffle;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class WriteToFileBolt extends BaseBasicBolt {
  private static final Logger logger = LoggerFactory.getLogger(WriteToFileBolt.class);
  private PrintWriter writer;

  @Override
  public void prepare(Map stormConf, TopologyContext context) {
    String fileName =
        String.format(
            "%soutput-%s-%s.txt",
            stormConf.get("dirToWrite"), context.getThisTaskId(), context.getThisComponentId());
    try {
      writer = new PrintWriter(fileName, "UTF-8");
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      logger.error("Unable to initialize PrintWriter", e);
    }
  }

  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    Integer integer = input.getIntegerByField("integer");
    Integer bucket = input.getIntegerByField("bucket");
    String value = String.format("%s-%s", integer.toString(), bucket.toString());
    collector.emit(new Values(value));
    writer.println(value);
    writer.flush();
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("field"));
  }

  @Override
  public void cleanup() {
    writer.flush();
    writer.close();
  }
}
