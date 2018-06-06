package com.github.brosgarden.grouping.direct;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;

public class DirectGroupingSpout extends BaseRichSpout {
  private SpoutOutputCollector collector;
  private int integer = 0;
  private List<Integer> boltIds;

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    this.collector = collector;
    this.boltIds = context.getComponentTasks("write-bolt");
  }

  @Override
  public void nextTuple() {
    while (integer < 100) {
      int bucket = integer / 10;
      int taskId = boltIds.get(getBoltId(bucket));
      collector.emitDirect(taskId, new Values(integer, bucket));
      integer++;
    }
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("integer", "bucket"));
  }

  private int getBoltId(int bucket) {
    return bucket % boltIds.size();
  }
}
