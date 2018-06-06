package com.github.brosgarden.multi;

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

public class FileReaderSpout extends BaseRichSpout {
  private static final Logger logger = LoggerFactory.getLogger(FileReaderSpout.class);
  private SpoutOutputCollector collector;
  private boolean completed = false;
  private FileReader fileReader;
  private BufferedReader bufferedReader;

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    try {
      this.fileReader = new FileReader(conf.get("fileToRead").toString());
    } catch (FileNotFoundException e) {
      String message = String.format("Error Reading file [%s]", conf.get("fileToRead").toString());
      logger.error(message, e);
    }
    this.bufferedReader = new BufferedReader(fileReader);
    this.collector = collector;
  }

  @Override
  public void nextTuple() {
    if (!completed) {
      try {
        String line = bufferedReader.readLine();
        if (line == null) {
          completed = true;
          bufferedReader.close();
        } else {
          collector.emit(new Values(line));
        }
      } catch (IOException e) {
        logger.error("Could not fetch next tuple", e);
      }
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("word"));
  }
}
