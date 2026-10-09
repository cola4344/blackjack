package model;

public enum Suit {
    SPADE("\u2660","黑桃"),
    HEART("\u2665", "红桃"),
    CLUB("\u2663", "梅花"),
    DIAMOND("\u2666", "方块");//枚举牌的花色

    private  final String symbol;
    private  final String chinese;

    Suit(String symbol, String chinese) {
        this.symbol = symbol;
        this.chinese = chinese;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getChinese() {
        return chinese;
    }
}
