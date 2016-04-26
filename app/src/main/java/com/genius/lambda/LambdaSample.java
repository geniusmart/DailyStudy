package com.genius.lambda;

import java.util.Comparator;

public class LambdaSample {
    Comparator<String> mComparator = new Comparator<String>() {
        @Override
        public int compare(String lhs, String rhs) {
            return 0;
        }
    };

    public void test() {
        //Comparator<String> comp = (first, second) -> Integer.compare(first.length(), second.length());
    }
}
