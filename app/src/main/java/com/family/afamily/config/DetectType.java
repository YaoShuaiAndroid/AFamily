package com.family.afamily.config;

/**
 * Created by ys on 2016/9/11.
 */
public enum DetectType {
    ONE(1,"先天智能","先天智能"),
    TWO(2,"行为自检","行为自检"),
    THREE(3,"育儿指南","育儿指南");

    int value;

    String category;

    String name;

    DetectType(int value, String category, String name){
        this.value = value;
        this.category = category;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public static String[] getAllNames(){
        int length = DetectType.values().length;
        String[] names = new String[length];

        for (int i = 0; i < length; i++) {
            names[i] = DetectType.values()[i].getName();
        }

        return names;
    }

    public static String[] getTabNames(){
        String[] names = new String[4];

        for (int i = 0; i < 4; i++) {
            names[i] = DetectType.values()[i].getName();
        }

        return names;
    }

    public static int getMultiType(DetectType dataType){
        int retVal;
        switch (dataType){
            case ONE:
                retVal = 1;
                break;
            case TWO:
                retVal = 2;
                break;
            case THREE:
                retVal = 3;
                break;
            default:
                retVal = 4;
                break;
        }

        return retVal;
    }

}
