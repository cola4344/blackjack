package game;

import model.Card;
import model.Dealer;
import model.Player;
import model.Shoe;
import util.Consoleio;
import view.Consoleview;

import java.util.Random;

/**
 * 游戏主流程编排类。
 * 它只负责"按顺序调用"下注、发牌、玩家回合、庄家回合、结算，
 * 具体规则分别落在 Hand / Participant / Settler 中，避免逻辑堆在一处。
 */
public class BlackjackGame {
    private static final int INITIAL_CHIPS = 1000;//初始的筹码
    private static final int MIN_BET = 10;//最低下注
    private static final int DECK_COUNT = 4;//牌堆数

    private final Consoleio io;
    private final Consoleview view;
    private final Random random;//防止外部对这些属性进行修改
    private Player player;
    private Dealer dealer;
    private Shoe shoe;//因为每局游戏这三个都不一样所以不用final修饰

    public BlackjackGame(Consoleio io, Consoleview view, Random random) {
        this.io = io;
        this.view = view;
        this.random = random;
    }

    public void start() {
        view.printTitle();
        String name = io.readLine("请输入你的昵称：");
        this.player = new Player(name, INITIAL_CHIPS);
        this.dealer = new Dealer("庄家");
        // 从牌靴取牌
        this.shoe = new Shoe(DECK_COUNT, random);

        System.out.printf("初始筹码：%d，牌靴：%d 副牌（共 %d 张）%n",
                INITIAL_CHIPS, DECK_COUNT, shoe.total());
        System.out.println("规则：A 可作 1 或 11 点；点数超过 21 立即判负；庄家点数小于 17 必须继续要牌。");
        System.out.printf("下注区间：%d ~ 你的全部筹码。%n", MIN_BET);

        boolean running = true;//这里的running的作用是便于后面结束游戏，结束游戏只需要将running修改为false就可以了
        while (running) {
            if (player.getChips() < MIN_BET) {
                view.printGameOver("筹码不足以继续下注");
                break;
            }
            playOneRound();
            running = io.confirm("是否继续下一轮？");
        }
        view.printChips(player);//在游戏结束后打印剩余的筹码
        view.printMessage("感谢游玩！");
    }

    /**
     * 完成一轮完整对局。
     */
    private void playOneRound() {
        view.printSeparator();
        int bet = io.readInt("请输入本轮押注（" + MIN_BET + " ~ " + player.getChips() + "）：",
                MIN_BET, player.getChips());
        player.bet(bet);
        view.printStatus(player, bet);

        prepareRound();
        dealInitialCards();

        view.printDealerHidden(dealer);
        view.printHand(player.getName(), player.getHand().toString(), player.getHandValue());

        boolean doubled = false;

        // 玩家或庄家开局直接拿到 Blackjack，无需操作，直接进入结算
        if (!player.hasBlackjack() && !dealer.hasBlackjack()) {
            doubled = playPlayerTurns(bet);//通过方法返回true和false
            if (doubled) {
                bet *= 2;
            }
        }

        if (!player.isBust()) {
            playDealerTurn();
        }

        settle(bet, doubled);
    }

    /**
     * 清空上一轮手牌，并在需要时重洗牌靴。
     */
    private void prepareRound() {
        player.resetHand();
        dealer.resetHand();
        if (shoe.passHalf()) {
            shoe.reset();
            System.out.println("[系统] 已发出超过一半的牌，自动切牌并重新洗牌。");
        }
    }

    /**
     * 开局各发两张牌，玩家先拿，庄家第二张为暗牌。
     */
    private void dealInitialCards() {
        player.receive(shoe.draw());
        dealer.receive(shoe.draw());
        player.receive(shoe.draw());
        dealer.receive(shoe.draw());//这里发牌的顺序是为了仿照真实，可以交换顺序
    }

    /**
     * 玩家回合。
     *
     * @return 是否使用了双倍下注
     */
    private boolean playPlayerTurns(int bet) {
        // 首轮提供双倍下注选项
        if (player.getChips() >= bet) {
            String choice = io.readOption(
                    "请选择操作：hit(要牌) / stand(停牌) / double(双倍下注)：",
                    "hit", "stand", "double");
            if ("double".equals(choice)) {
                player.bet(bet);
                Card card = shoe.draw();
                player.receive(card);
                System.out.printf("[双倍下注] 你额外押注 %d，抽到 %s%n", bet, card.getDisplay());
                view.printHand(player.getName(), player.getHand().toString(), player.getHandValue());
                if (player.isBust()) {
                    System.out.println("点数超过 21，爆牌！");
                }
                return true;
            }
            if ("stand".equals(choice)) {
                return false;
            }
        }

        //这里是hit选项，因为前面两个if已经涵盖了double和stand所以不需要在这里家hit判断
        while (true) {
            String choice = io.readOption("请选择操作：hit(要牌) / stand(停牌)：", "hit", "stand");//在第二轮中不添加double
            if ("stand".equals(choice)) {
                break;
            }
            Card card = shoe.draw();//抽牌
            player.receive(card);
            System.out.printf("你抽到 %s%n", card.getDisplay());
            view.printHand(player.getName(), player.getHand().toString(), player.getHandValue());
            if (player.isBust()) {
                System.out.println("点数超过 21，爆牌！");
                break;
            }
            if (player.getHandValue() == 21) {
                System.out.println("已达到 21 点，自动停牌。");
                break;
            }
        }
        return false;
    }

    /**
     * 庄家回合：小于 17 必须继续要牌，大于等于 17 强制停牌。
     */
    private void playDealerTurn() {
        System.out.println("--------------------------------------------------");
        System.out.printf("%s 亮出暗牌：%s  -> 点数 %d%n",
                dealer.getName(), dealer.getHand().toString(), dealer.getHandValue());
        while (dealer.wantsHit()) {
            Card card = shoe.draw();
            dealer.receive(card);
            System.out.printf("%s 抽到 %s%n", dealer.getName(), card.getDisplay());
            view.printHand(dealer.getName(), dealer.getHand().toString(), dealer.getHandValue());
        }
        if (dealer.isBust()) {
            System.out.println("庄家点数超过 21，爆牌！");
        }
    }
    /** 结算并返还筹码。 */
    private void settle(int bet, boolean doubled) {
        view.printSeparator();
        view.printHand(player.getName(), player.getHand().toString(), player.getHandValue());
        view.printHand(dealer.getName(), dealer.getHand().toString(), dealer.getHandValue());//分别展示双方的手牌和点数

        Outcome outcome = Settler.judge(
                player.isBust(),
                dealer.isBust(),
                player.hasBlackjack(),
                dealer.hasBlackjack(),
                player.getHandValue(),
                dealer.getHandValue());//判断是那种胜利方式并给出对应的赔率

        int returned = Settler.payout(bet, outcome);//计算得到的筹码
        player.addChips(returned);

        System.out.println("本轮结果：" + outcome.getMessage());
        if (outcome == Outcome.PUSH) {
            System.out.printf("退还押注 %d%n", returned);
        } else if (outcome.isPlayerWin()) {
            System.out.printf("赢得 %d 筹码（含本金）%n", returned);
        } else {
            System.out.printf("损失 %d 筹码%n", bet);
        }
        if (doubled) {
            System.out.println("（本轮使用了双倍下注）");
        }
        view.printChips(player);
    }
}
