package model;

import java.util.Objects;

public class Card {
    private final Suit suit;
    private final Rank rank;
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }//将rank和suit中的点数以及花色记录在card中，使card可以调用suit和rank中的方法


    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return getDisplay();
    }
    public String getDisplay() {
        return suit.getSymbol() + rank.getLabel();
    }//展示牌的花色和点数
}
