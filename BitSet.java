package cn.eyecool.minisearch.memory;

import cn.eyecool.minisearch.function.Consumer;

public class BitSet {

    private final static int ADDRESS_BITS_PER_WORD = 5;

    private final static int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private static final int[] BITS = {
            0x00000001,
            0x00000002,
            0x00000004,
            0x00000008,
            0x00000010,
            0x00000020,
            0x00000040,
            0x00000080,
            0x00000100,
            0x00000200,
            0x00000400,
            0x00000800,
            0x00001000,
            0x00002000,
            0x00004000,
            0x00008000,
            0x00010000,
            0x00020000,
            0x00040000,
            0x00080000,
            0x00100000,
            0x00200000,
            0x00400000,
            0x00800000,
            0x01000000,
            0x02000000,
            0x04000000,
            0x08000000,
            0x10000000,
            0x20000000,
            0x40000000,
            0x80000000
    };

    private final int capacity;
    private final int[] words;
    private volatile int nextWordIndex;

    BitSet(int capacity) {
        this.capacity = capacity;
        this.words = new int[(capacity + BITS_PER_WORD - 1) / BITS_PER_WORD];
    }

    void set(int bitIndex) {
        if (bitIndex < capacity) {
            int wordIndex = wordIndex(bitIndex);
            words[wordIndex] |= (1L << (bitIndex % BITS_PER_WORD));
        }
    }

    void clear(int bitIndex) {
        if (bitIndex < capacity) {
            int wordIndex = wordIndex(bitIndex);
            words[wordIndex] &= ~(1L << (bitIndex % BITS_PER_WORD));
            nextWordIndex = wordIndex;
        }
    }

    public boolean get(int bitIndex) {
        if (bitIndex < capacity) {
            int wordIndex = wordIndex(bitIndex);
            return (words[wordIndex] & (1L << (bitIndex % BITS_PER_WORD))) != 0;
        }

        return false;
    }

    public int nextClear() {
        int i = nextWordIndex;
        int start = i;
        int end = words.length;
        for (; i < end; ) {
            if (words[i] != -1) {
                nextWordIndex = i;
                for (int j = 0; j < BITS_PER_WORD; j++) {
                    if ((words[i] & (1L << BITS_PER_WORD)) == 0) {
                        return i * BITS_PER_WORD + j;
                    }
                }
            }

            i++;

            if (i == words.length) {
                i = 0;
                end = start;
            }
        }

        return -999;
    }

    void forEach(Consumer<Integer> consumer) {
        for (int i = 0; i < words.length; i++) {
            int word = words[i];
            if (word != 0) {
                for (int bitIndex = 0; bitIndex < BITS_PER_WORD; bitIndex++) {
                    if ((word & BITS[bitIndex]) != 0) {
                        consumer.accept(i * BITS_PER_WORD + bitIndex);
                    }
                }
            }
        }

    }

    private int wordIndex(int bitIndex) {
        return bitIndex >> ADDRESS_BITS_PER_WORD;
    }

    BitSet reverse() {
        throw new UnsupportedOperationException("Not Implement yet.");
    }

    BitSet copy() {
        throw new UnsupportedOperationException("Not Implement yet.");
    }

    void and(BitSet bitSet) {
        if (bitSet.words.length != this.words.length) {
            throw new IllegalArgumentException("BitSet's length doesn't match.");
        }

        for (int i = 0; i < words.length; i++) {
            this.words[i] &= bitSet.words[i];
        }
    }

    void or(BitSet bitSet) {
        if (bitSet.words.length != this.words.length) {
            throw new IllegalArgumentException("BitSet's length doesn't match.");
        }

        for (int i = 0; i < words.length; i++) {
            this.words[i] |= bitSet.words[i];
        }
    }

    void unset() {
        for (int i = 0; i < words.length; i++) {
            words[i] = 0;
        }
    }

    void set() {
        for (int i = 0; i < words.length; i++) {
            words[i] = 0xFFFFFFFF;
        }
    }
}
