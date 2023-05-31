package com.example.sw_alrami;

import java.util.List;

public class Data {
    private List<Value> values;
    private boolean hasNext;
    private Number lastIndex;

    // getters and setters

    @Override
    public String toString() {
        return "Data{" +
                "values=" + values +
                ", hasNext=" + hasNext +
                ", lastIndex=" + lastIndex +
                '}';
    }
}