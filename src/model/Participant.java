package model;

public abstract class Participant {
    private final String name;
    private final Hand hand = new Hand();
    private int chips;//chip是筹码
    protected Participant(String name, int chips) {
        this.name = name;
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public int getChips() {
        return chips;
    }
    public void addChips(int amount) { this.chips += amount; }//游戏胜利后得到的筹码

public void bet(int amount) {
    if (amount > chips) {
        throw new IllegalArgumentException("筹码不足");//这里是因为手上的筹码不足而导致的异常，是状态异常
    }
    this.chips -= amount;//得到下注的结果，用手上的chips-amount返回剩下的chips
}
    public boolean canAfford(int amount) {
        return amount > 0 && amount <= chips;
    }//查询是否可以承担下注
    public void resetHand() { hand.clear(); }//清空手牌
    public void receive(Card card) { hand.add(card); }//拿牌
    public int getHandValue() { return hand.getValue(); }//计算手中牌的点数之和
    public boolean isBust() { return hand.isBust(); }//判断点数有没有大于21
    public boolean hasBlackjack() { return hand.isBlackjack(); }//判断手里有没有blackjack牌型
    public abstract boolean wantsHit();//让玩家和庄家重写该方法用于加注
}

