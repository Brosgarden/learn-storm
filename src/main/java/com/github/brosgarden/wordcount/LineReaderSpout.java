package com.github.brosgarden.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class LineReaderSpout extends BaseRichSpout {
  private static final Logger logger = LoggerFactory.getLogger(LineReaderSpout.class);
  private SpoutOutputCollector collector;
  private boolean completed = false;
  private FileReader fileReader;
  private BufferedReader bufferedReader;

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    try {
      this.fileReader = new FileReader(conf.get("fileToRead").toString());
    } catch (FileNotFoundException e) {
      logger.error(String.format("unable to find file %s", conf.get("fileToRead")), e);
    }
    this.collector = collector;
    this.bufferedReader = new BufferedReader(fileReader);
  }

  @Override
  public void nextTuple() {
    if (!completed) {
      try {
        String line = bufferedReader.readLine();
        if (line == null) {
          completed = true;
          bufferedReader.close();
          fileReader.close();
        } else {
          collector.emit(new Values(line));
        }
      } catch (IOException e) {
        logger.error("Bad thing happened", e);
      }
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
      declarer.declare(new Fields("line"));
  }
}
