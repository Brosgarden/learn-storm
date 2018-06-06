package com.github.brosgarden.java8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Streams {

  public static void main(String[] args) {
    Streams streams = new Streams();

    List<Bean> beanList = streams.getBeanList();
    List<Bean> filteredBeanList = streams.getFilteredBeanList();

    Map<Long, List<Bean>> mapWithoutStream = streams.mapBeansWithoutStream(filteredBeanList);
    Map<Long, List<Bean>> mapWithStream = streams.mapBeansWithStream(filteredBeanList);
    System.out.println(mapWithoutStream.size());
    System.out.println(mapWithStream.size());

    if (mapWithoutStream.equals(mapWithStream)) {
      System.out.println("Equal");
    } else {
      System.out.println("not equal");
    }

    streams.printMap(mapWithoutStream);
    streams.printMap(mapWithStream);
  }

  private List<Bean> getBeanList() {
    List<Bean> beanList = new ArrayList<>();
    beanList.add(new Bean(1, 3, "value"));
    beanList.add(new Bean(2, 3, "valueTwo"));
    beanList.add(new Bean(3, 3, "valueThree"));
    beanList.add(new Bean(4, 2, "value"));
    beanList.add(new Bean(5, 2, "valueTwo"));
    beanList.add(new Bean(6, 1, "value"));
    return beanList;
  }

  private Map<Long, List<Bean>> mapBeansWithStream(List<Bean> beanList) {
    return beanList.stream().collect(groupingBy(Bean::getOtherId));
  }

  private Map<Long, List<Bean>> mapBeansWithoutStream(List<Bean> beanList) {
    Map<Long, List<Bean>> beanMap = new HashMap<>();

    for (Bean bean : beanList) {
      Long otherId = bean.getOtherId();
      List<Bean> beanListForOtherId = beanMap.get(otherId);
      if (beanListForOtherId == null) {
        beanListForOtherId = new ArrayList<>();
        beanListForOtherId.add(bean);
        beanMap.put(otherId, beanListForOtherId);
      } else {
        beanListForOtherId.add(bean);
      }
    }
    return beanMap;
  }

  private void printMap(Map<Long, List<Bean>> beanMap) {
    BiConsumer<Long, List<Bean>> biConsumer =
        (key, value) -> System.out.println("Key: " + key + " value: " + value);
    beanMap.forEach(biConsumer);
  }

  private List<Bean> getFilteredBeanList() {
    return getBeanList().stream().filter(s -> s.getValue().contains("Two")).collect(toList());
  }
}
