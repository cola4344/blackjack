package game;
public enum Outcome {
        PLAYER_BLACKJACK("玩家 Blackjack 获胜！", 2.5),
        PLAYER_WIN("玩家获胜", 2.0),
        DEALER_BUST("庄家爆牌，玩家获胜", 2.0),
        PUSH("平局，退还筹码", 1.0),//平局时赔率用1是因为前面在下注是将筹码减去了，这里需要将下注的筹码补回来
        PLAYER_BUST("玩家爆牌，庄家获胜", 0.0),
        DEALER_BLACKJACK("庄家 Blackjack，庄家获胜", 0.0),
        DEALER_WIN("庄家获胜", 0.0);//枚举承载了message和payoutRate的数据，这样用枚举而不是用类来接收数据可以避免if-else循环

        private final String message;
        private final double payoutRate;

        Outcome(String message, double payoutRate) {
            this.message = message;
            this.payoutRate = payoutRate;
        }

        public String getMessage() { return message; }
        public double getPayoutRate() { return payoutRate; }

        public boolean isPlayerWin() {
            return this == PLAYER_BLACKJACK || this == PLAYER_WIN || this == DEALER_BUST;
        }//判断玩家是否获胜
    }

