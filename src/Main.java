import game.BlackjackGame;
import util.Consoleio;
import view.Consoleview;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Consoleio io = new Consoleio();
        Consoleview view = new Consoleview();
        Random random = new Random();//创造依赖

        try {
            new BlackjackGame(io, view, random).start();//创建了匿名对象，因为后面没有地方需要进行引用
        } catch (Exception e) {
            // 任何未预期异常都不允许以堆栈崩溃的形式暴露给用户
            view.printError("程序遇到未预期的问题，已安全退出。");
            System.err.println("详细信息：" + e.getMessage());//Comsoleio中处理的是用户输入形式的异常，main方法中输出的
            //是程序自己本身的异常
        } finally {//无论发生异常与否都会做到关流
            io.close();
        }
    }
}