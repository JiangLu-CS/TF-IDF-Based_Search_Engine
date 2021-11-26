package com.cose.ir.exp.bean;

import java.util.LinkedList;

public class Item {
    public String term;
    public Integer docs;
    public Integer freq_total;
    public LinkedList<Item_ori> ori_item_list;

    public Item(String term, Integer freq_total, Item_ori current) {
        this.term = term;
        this.freq_total = freq_total;
        this.docs = 1;
        ori_item_list = new LinkedList<>();
        ori_item_list.add(current);
    }
}
