package game;

public final class Settler  {
//这里用final修饰是作工具类来进行使用
    private Settler() {
    }

    public static Outcome judge(boolean playerBust,
                                boolean dealerBust,
                                boolean playerBlackjack,
                                boolean dealerBlackjack,
                                int playerValue,
                                int dealerValue) {
        if (playerBust) {
            return Outcome.PLAYER_BUST;
        }//玩家爆牌必须要排在第一，排在后面会导致牌数不对导致逻辑错误
        if (dealerBlackjack && !playerBlackjack) {
            return Outcome.DEALER_BLACKJACK;
        }
        if (playerBlackjack && !dealerBlackjack) {
            return Outcome.PLAYER_BLACKJACK;
        }
        if (dealerBust) {
            return Outcome.DEALER_BUST;
        }
        if (playerValue > dealerValue) {
            return Outcome.PLAYER_WIN;
        }
        if (playerValue == dealerValue) {
            return Outcome.PUSH;
        }
        return Outcome.DEALER_WIN;
    }


    public static int payout(int bet, Outcome outcome) {
        return (int) Math.round(bet * outcome.getPayoutRate());
    }/*这里先用math.round将后面int与double的计算结果进行四舍五入并返回了long数据类型，之所以要进行四舍五入是因为在运算时可能出现100*2.5=249.9999
    的情况，这样操作可以有效避免该问题发生，最后将long强转为int*/
}
