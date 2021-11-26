package com.cose.ir.exp.bean;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class Item_ori implements Comparable<Item_ori> {
    public String term;
    public Integer docId;
    public Integer freq;

    public Item_ori(String term, Integer docId) {
        this.term = term;
        this.docId = docId;
        this.freq = 1;
    }

    @Override
    public int compareTo(Item_ori o) {
        Comparator<Object> CHINA_COMPARE = Collator.getInstance(Locale.CHINA);
        int result = ((Collator) CHINA_COMPARE).compare(term, o.term);
        if (result == 0) {
            if (docId < o.docId) {
                return -1;
            } else if (docId > o.docId) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return result;
        }
    }
}
