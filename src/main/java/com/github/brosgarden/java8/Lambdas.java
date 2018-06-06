package com.github.brosgarden.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Lambdas {

  public static void main(String[] args) throws Exception {
    Greeter helloGreeter = new HelloWorldGreeter();
    Greeter lambdaGreeter = new HelloLambdaGreeter();
    Lambdas lambdas = new Lambdas();

    List<Callable<String>> callableList = new ArrayList<>();
    callableList.add(lambdas.prepareCallable(helloGreeter));
    callableList.add(lambdas.prepareCallable(lambdaGreeter));

    ExecutorService service = Executors.newFixedThreadPool(2);

    List<Future<String>> results = service.invokeAll(callableList);
    Thread.sleep(1000);

    for (Future<String> result : results) {
      System.out.println(result.get());
    }
  }

  private Callable<String> prepareCallable(Greeter greeter) {
    return new Callable<String>() {
      @Override
      public String call() throws Exception {
        return greeter.greet();
      }
    };
  }

  private Callable<String> prepareCallableLambda(Greeter greeter) {
    return () -> greeter.greet();
  }

  private Callable<String> prepareCallableMethodReference(Greeter greeter) {
    return greeter::greet;
  }
}
