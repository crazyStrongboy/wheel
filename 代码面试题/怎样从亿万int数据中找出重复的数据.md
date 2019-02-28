```

/**
 * 判断10亿个int数里面是否有重复的数据
 * 1. int数的范围 -2^31 < num < 2^31-1
 * 2. 把数字加上 1L << 31 变成正数，用正数进行取模（取出来的时候减去 1L << 31 获取原值即可）
 * 3. 找出存放的byte数组的byteArrayIndex
 * 4. 取出byte[byteArrayIndex],查询当前byte的byteIndex索引位置是否已经填充值
 * 5. 用secondInts记录是否重复出现
 */
public class BigMap {

    private static byte[] firstInts = new byte[1 << 29];
    private static byte[] secondInts = new byte[1 << 29];

    /**
     * 将int数字约束到上面byte数组的范围内
     * 1<<32 bit = 1<<29 byte (1byte == 8bit)
     */
    private static Integer BYTE_BIT_SIZE = 8;

    public static void main(String[] args) {
        BigMap bigMap = new BigMap();
        bigMap.splitData(5);
        bigMap.splitData(5);
        bigMap.splitData(6);
        bigMap.splitData(8);
        bigMap.splitData(8);
        bigMap.splitData(8);
        bigMap.splitData(9);
        bigMap.splitData(9);
        bigMap.printRepetitionData();
    }

    private void splitData(int num) {
        long bitIndex = num + (1l << 31);
        int byteArrayIndex = (int) (bitIndex / BYTE_BIT_SIZE);
        int byteIndex = (int) (bitIndex & (BYTE_BIT_SIZE - 1));
        int first = (firstInts[byteArrayIndex]) & (1 << byteIndex);
        if (first == 0) {
            firstInts[byteArrayIndex] = (byte) (firstInts[byteArrayIndex] | (1 << byteIndex));
        } else {
            secondInts[byteArrayIndex] = (byte) (secondInts[byteArrayIndex] | (1 << byteIndex));
        }
    }

    private void printRepetitionData() {
        for (int i = 0; i < firstInts.length; i++) {
            for (int j = 0; j < 8; j++) {
                int first = firstInts[i] & (1 << j);
                if (first != 0) {
                    // 证明这个点存储了数据
                    int second = secondInts[i] & (1 << j);
                    // 判断是否重复
                    if (second != 0){
                        // 取出数据
                        long result = ((long) i * 8 + j) - (1l << 31);
                        System.out.println(result);
                    }
                }
            }
        }
    }
}


```