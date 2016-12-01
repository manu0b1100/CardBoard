package com.example.manobhavjain.projectkasm;

import java.util.ArrayList;

/**
 * Created by Manobhav Jain on 11/17/2016.
 */

public class StatusData {
    private String username;
    private ArrayList<CardidObject>card_id=new ArrayList<>();

    public ArrayList<CardidObject> getCard_id() {
        return card_id;
    }
}
class CardidObject{
    String key;

    public CardidObject(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardidObject that = (CardidObject) o;

        if(key.equals(that.key))
            return true;
        else
            return false;

    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
