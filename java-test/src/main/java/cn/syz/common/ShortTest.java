package cn.syz.common;

public class ShortTest {

    int i;
    short s;
    int[] ia = new int[1];
    short[] sa = new short[1];

    public static void main(String[] args) {
        ShortTest test = new ShortTest();
        test.i = 2;
        test.s = 3;
        test.i = test.s;
        test.s = (short) test.i;
        test.ia[0] = 2;
        test.sa[0] = 3;
        boolean b;
        b = test.i < 10;
        b = test.s < 10;
        b = test.ia[0] < 10;
        b = test.sa[0] < 10;
        if (b) {
            System.out.println("finished");
        }
        // short[] ss = new short[1];
        // ss[0] = 999; // sastore
        // int[] is = new int[1];
        // is[0] = 999; // iastore
    }

}
