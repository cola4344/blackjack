package view;


import model.Dealer;
import model.Player;

public class Consoleview {
    private static final String LINE = "==================================================";

    public void printTitle() {
        System.out.println(LINE);
        System.out.println("           控制台 21 点 (Blackjack)");
        System.out.println(LINE);
    }

    public void printSeparator() {
        System.out.println(LINE);
    }

    public void printHand(String owner, String cards, int value) {
        System.out.printf("%s 的手牌：%s  -> 当前点数 %d%n", owner, cards, value);
    }

    public void printDealerHidden(Dealer dealer) {
        System.out.printf("%s 的手牌：%s  -> 明牌点数 %d%n",
                dealer.getName(),
                dealer.getHand().toHiddenString(),
                dealer.getHand().getFirst().getRank().getBasePoint());
    }

    public void printStatus(Player player ,int bet) {
        System.out.println("--------------------------------------------------");
        System.out.printf("当前押注：%d    你的筹码：%d%n", bet, player.getChips());
        System.out.println("--------------------------------------------------");
    }

    public void printChips(Player player) {
        System.out.printf("%s 剩余筹码：%d%n", player.getName(), player.getChips());
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printError(String message) {
        System.out.println("[提示] " + message);
    }

    public void printGameOver(String reason) {
        System.out.println(LINE);
        System.out.println("游戏结束：" + reason);
        System.out.println(LINE);
    }
}
