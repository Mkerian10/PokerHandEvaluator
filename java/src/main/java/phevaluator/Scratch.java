package phevaluator;

public class Scratch{

  public static void main(String[] args){
    // Community cards
    int a = 7 * 4; // 9c
    int b = 2 * 4; // 4c
    int c = 2 * 4 + 3; // 4s
    int d = 7 * 4 + 1; // 9d
    int e = 2 * 4 + 2; // 4h

// Player 1
    int f = 10 * 4; // Qc
    int g = 4 * 4; // 6c

// Player 2
    int h = 0 + 0; // 2c
    int i = 7 * 4 + 2; // 9h

    int rank1, rank2;

    rank1 = Evaluate7.evaluate7Cards(a, b, c, d, e, f, g);
    rank2 = Evaluate7.evaluate7Cards(a, b, c, d, e, h, i);

    System.out.println(rank1 < rank2);
    System.out.println(rank1);
    System.out.println(rank2);


  }
}
