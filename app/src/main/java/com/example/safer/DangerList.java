package com.example.safer;

import java.util.ArrayList;

public class DangerList {
    ArrayList<String> Dangers = new ArrayList<>();

    public DangerList(){
    }

    public ArrayList<String> getDangers() {
        return Dangers;
    }

    public void PushDanger(String dangerId){
        this.Dangers.add(dangerId);
    }
}
