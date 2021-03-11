package evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BatchEvaluator{

  public BatchEvaluator(int nThreads){
    if(nThreads == 0){
      nThreads = Runtime.getRuntime().availableProcessors();
    }
    this.nThreads = nThreads;
    executor = Executors.newFixedThreadPool(nThreads);
  }

  private final int nThreads;

  private final ExecutorService executor;

  public List<BatchElement> submitBatch(List<BatchElement> inputs){
    int nBatches = nThreads;
    var batches = reduce(inputs, nBatches, Evaluation.defaultFn());
    System.out.println("Beginning processing of " + inputs.size() + " inputs.");

    CompletionService<?> completionService = new ExecutorCompletionService<>(executor);
    var futures = batches.stream().map(r -> completionService.submit(r, null)).collect(Collectors.toList());

    for(int i = 0; i < futures.size(); i++){
      try{
        completionService.take();
        System.out.println(String.format("Completed %d of %d batches.", i + 1, nBatches));

      }catch(InterruptedException e){
        e.printStackTrace();
      }
    }

    return inputs;
  }

  private List<Runnable> reduce(List<BatchElement> inputs, int nBatches, Evaluation evalFunc){
    int batchSize = inputs.size() / nBatches;
    List<Runnable> batches = new ArrayList<>(nBatches);

    for(int i = 0; i < nBatches; i++){
      int lowerBound = i * batchSize;
      int upperBound = (i + 1) * batchSize;

      Runnable batch = () -> {
        for(int j = lowerBound; j < upperBound; j++){
          evalFunc.evaluate(inputs.get(j));
        }
      };

      batches.add(batch);
    }

    return batches;
  }

  interface Evaluation{
    void evaluate(BatchElement element);

    static Evaluation defaultFn(){
      return e -> e.setResult(Evaluator.evaluate(e.inputs));
    }
  }

}
