package com.github.brosgarden.write;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class FilterAndWriteFieldsBolt extends BaseBasicBolt {
  private static final Logger logger = LoggerFactory.getLogger(FilterAndWriteFieldsBolt.class);
  private PrintWriter printWriter;

  @Override
  public void prepare(Map stormConf, TopologyContext context) {
    String fileName =
        "output" + context.getThisTaskId() + "-" + context.getThisComponentId() + ".log";
    try {
      printWriter = new PrintWriter(stormConf.get("dirToWrite") + fileName, "UTF-8");
    } catch (IOException e) {
      logger.error("Unable to initialize PrintWriter", e);
    }

    super.prepare(stormConf, context);
  }

  @Override
  public void execute(Tuple input, BasicOutputCollector collector) {
    String firstName = input.getStringByField("firstName");
    String lastName = input.getStringByField("lastName");
    String line = firstName + "," + lastName;
    printWriter.println(line);
    printWriter.flush();
    collector.emit(new Values(firstName, lastName));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("firstName", "lastName"));
  }
}
