package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
public class Shoe {
    private final List<Card> cards = new ArrayList<>();//方便后续对card进行操作
    private final int deckCount;//有多少副牌
    private final Random random;

    public Shoe(int deckCount, Random random) {
        if (deckCount < 1) {
            throw new IllegalArgumentException("牌副数必须大于 0");//如果小于了1为状态异常
        }
        this.deckCount = deckCount;
        this.random = random;
        fill();//将牌组装满
        shuffle();//将牌组打乱
    }

    private void fill() {
        cards.clear();
        for (int i = 0; i < deckCount; i++) {
            for (Suit suit : Suit.values()) {      // 枚举自带的 values() 方法，它将枚举类中的所有对象抽取到一个数组中并返回
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));//为牌加上花色和数值
                }
            }
        }
    }//生成数量为的deckcount副牌
    public void shuffle() {
        Collections.shuffle(cards, random);
    }//打乱牌
    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("牌靴已空");
        }//防止牌库空了后继续抽牌产生空指针
        return cards.remove(cards.size() - 1);
    }//将cards集合的最后一位作为牌返回给调用者
    public int getDeckCount() { return deckCount; }//记录有多少副牌
    public int remaining() { return cards.size(); }//得到手中还有几张牌
    public int total() { return deckCount * 52; }//所有牌的数量
    public boolean passHalf() {
        return remaining() < total() / 2.0;
    }//判断牌库中的牌有没有抽到一半
    public void reset() {
        fill();
        shuffle();
    }//重启该游戏，重新将牌库填满并随机打乱
}
