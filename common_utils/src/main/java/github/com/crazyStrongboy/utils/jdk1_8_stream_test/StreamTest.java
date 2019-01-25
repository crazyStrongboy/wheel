package github.com.crazyStrongboy.utils.jdk1_8_stream_test;

import java.util.ArrayList;

public class StreamTest {
    public static void main(String[] args) {
        testReduce();
    }

    public static void testReduce() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("a2");
        list.add("a22");
        list.add("a233");
        Integer reduce = list.parallelStream().reduce(0,
                (sum, str) -> sum + str.length(),
                (a, b) -> a + b); //并行计算时用来合并的
        System.err.println(reduce);
    }
}


class Employee {
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public void print() {
        System.err.println(name);
    }
}