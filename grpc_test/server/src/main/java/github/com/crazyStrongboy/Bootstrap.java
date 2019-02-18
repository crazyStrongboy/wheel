package github.com.crazyStrongboy;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * jdk 1.8  stream test
 *
 * @author mars_jun
 */
public class Bootstrap {
    public static void main(String[] args) {
//        System.out.println("start..........");
//        Runnable runnable = new Runnable(){
//
//            @Override
//            public void run() {
//
//            }
//        };
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("a2");
        list.add("a22");
        list.add("a233");
        Stream<String> stream = list.stream();
        Optional<String> max = stream.max(Comparator.comparing(String::length));
        String s = max.get();
        System.out.println("max ： " + s);
        stream = list.parallelStream();
        Integer reduce = stream.reduce(0,
                (sum, str) -> sum + str.length(),
                (a, b) -> a + b); //并行用来合并的
        System.err.println(reduce);
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        Integer reduce1 = list1.stream().reduce(0,
                (a, b) -> a + b);
        System.out.println(reduce1);
//        stream.map(s -> {
//            System.err.println(s);
//           return  s.toUpperCase()+" : hello";
//        }).forEach(s -> System.out.println(s));
        stream.filter((i) -> i.startsWith("a"))
                .distinct().forEach(a -> System.out.println(a));

//        System.err.println(Spliterator.SIZED | Spliterator.SUBSIZED);
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee("dadad"));
        employeeList.add(new Employee("111"));
        employeeList.add(new Employee("222"));
        employeeList.stream().forEach(Employee::print);


        Stream<String> stringStream = Stream.of("ada", "da", "1");
        List<String> stringList = stringStream.collect(Collectors.toList());
        System.out.println(stringList);

        List<Student> students = new ArrayList<>();
        students.add(new Student("zhangsan",50));
        students.add(new Student("lisi",10));
        students.add(new Student("wangwu",43));
        Map<Student, Integer> studentIntegerMap = students.stream().collect(Collectors.toMap(Function.identity(), student -> student.score));
        studentIntegerMap.forEach((key,value)-> System.out.println(key.name+"==="+value));

    }

    public static class Employee {
        private String name;

        public Employee(String name) {
            this.name = name;
        }

        public void print() {
            System.err.println(name);
        }
    }

    public static class Student {
        private String name;

        private Integer score;

        public Student(String name, Integer score) {
            this.name = name;
            this.score = score;
        }
    }
}
