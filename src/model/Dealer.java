package model;

public class Dealer extends Participant{
    private static final int DEALER_CHIPS = 1_000_000;//1000000这个量命名为dealere_chips
    public Dealer(String name) {
        super(name, DEALER_CHIPS);//每一次调用该构造都会将庄家的点数重置到1000000，相当于实现了庄家的点数无限
    }
    @Override
    public boolean wantsHit() {
        return getHandValue() < 17;
    }//满足了庄家的手牌点数必须要大于等于17的规则
}
