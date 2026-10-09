package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {
    private static final int BLACKJACK = 21;//定义常量21叫做balckjack

    private final List<Card> cards = new ArrayList<>();//创建card对象，方便后续对card操作

    public void add(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }/*将上述属于list的方法进行封装，使外部在调用这些方法时
需要满足内部的要求，隐藏了业务逻辑，使外部代码不会看着臃肿*/
    public Card getFirst() {
        return cards.get(0);
    }

    public void clear() {
        cards.clear();
    }

    public int getValue() {
        int total = 0;
        int aceCount = 0;
        for (Card card : cards) {
            total += card.getRank().getBasePoint();//记录手牌中点数的总和
            if (card.getRank().isAce()) {
                aceCount++;//记录ace的总和
            }
        }
        while (total > BLACKJACK && aceCount > 0) {
            total -= 10;
            aceCount--;
        }//实现大于21点后将ace从11变为1
        return total;//返回点数
    }
    public boolean isSoft() {
        boolean hasAce = false;
        for (Card card : cards) {
            if (card.getRank().isAce()) {
                hasAce = true;
                break;
            }
        }
        if (!hasAce) {
            return false;
        }//判断手牌中有没有ace
        int hard = 0;
        for (Card card : cards) {
            hard += card.getRank().isAce() ? 1 : card.getRank().getBasePoint();
        }
        return hard + 10 <= BLACKJACK;//如果这里大于balckjack了说明不符合软牌的规定返回false，反之则返回true
    }//判断ace应该当成1还是11用

    public boolean isBust() {
        return getValue() > BLACKJACK;
    }//判断点数有没有超过21

    public boolean isBlackjack() {
        return cards.size() == 2 && getValue() == BLACKJACK;
    }//判断调用者是不是满足黑杰克牌型
    public boolean canHit() {
        return !isBust() && getValue() < BLACKJACK;//判断能不能继续摸牌
    }
    public String toHiddenString() {
        if (cards.isEmpty()) {
            return "(空)";
        }//保护程序在游戏初始化时不会因为手里没有牌而崩溃
        StringBuilder builder = new StringBuilder();//stringbuilder便于后续的添加
        builder.append(cards.get(0).getDisplay());//展示手牌中第一张牌
        for (int i = 1; i < cards.size(); i++) {
            builder.append(" [暗牌]");
        }//将其它的牌设定为暗牌并打印出来
        return builder.toString();//将第一张牌和其他的暗牌以文字的形式打印出来
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) {
                builder.append(" ");
            }
            builder.append(cards.get(i).getDisplay());
        }
        return builder.toString();//将玩家手中的牌展示给自己看
    }
}


