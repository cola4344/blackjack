package model;

public enum Rank {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4",4),
    FIVE("5",5),
    SIX("6",6),
    SEVEN("7",7),
    EIGHT("8",8),
    NINE("9",9),
    TEN("10", 10),
    JACK("J", 10),
    QUEEN("Q", 10),
    KING("K", 10),
    ACE("A", 11);//枚举牌的点数

    private final String label;
    private final int basepoint;

     Rank(String label, int basepoint) {
        this.label = label;
        this.basepoint = basepoint;
    }

    public String getLabel() {
        return label;
    }

    public int getBasePoint() {
        return basepoint;
    }
    public boolean isAce() {
        return this == ACE;
    }//判断其他类调用该方法的牌是不是ace
}
