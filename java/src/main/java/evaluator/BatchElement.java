package evaluator;

public class BatchElement{

  public BatchElement(int batchId, int[] inputs){
    this.batchId = batchId;
    this.inputs = inputs;
  }

  private final int batchId;

  public final int[] inputs;

  private int result;

  public int getResult(){
    return result;
  }

  void setResult(int result){
    this.result = result;
  }


}
