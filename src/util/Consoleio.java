package util;

import java.util.Scanner;

    /**
     * 控制台输入工具。
     * 统一使用 nextLine() 读取整行再自行解析，
     * 避免 nextInt() 与 nextLine() 混用造成的"回车残留"问题。
     * 所有非法输入都在这里被拦截，业务层永远拿不到脏数据。
     */
    public class Consoleio {
        private final Scanner scanner;//创建私有对象,使其不能被修改

        public Consoleio() {
            this.scanner = new Scanner(System.in);
        }//构造方法的创建

        /** 0读取一个位于 [min, max] 区间的整数，非法输入引导重输。 */
        public int readInt(String prompt, int min, int max) {
            while (true) {//直到获得正确信息前一直重复
                System.out.print(prompt);
                String line = scanner.nextLine().trim();//一下读取一行避免出现行末的空格无法读取的情况，trim的作用是将输入的不是数字的数据给删除
                if (line.isEmpty()) {
                    System.out.println("[提示] 输入不能为空，请重新输入。");
                    continue;
                }
                try {
                    int value = Integer.parseInt(line);
                    if (value < min || value > max) {
                        System.out.printf("[提示] 请输入 %d 到 %d 之间的整数，当前输入：%s%n", min, max, line);
                        continue;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    // 用户输入了字母或其它非数字内容
                    System.out.printf("[提示] \"%s\" 不是合法整数，请重新输入。%n", line);
                }
            }
        }

        /** 读取一整行非空文本。 */
        public String readLine(String prompt) {
            while (true) {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    return line;
                }
                System.out.println("[提示] 输入不能为空，请重新输入。");
            }
        }

        /** 读取一个指定集合中的选项，忽略大小写。 */
        public String readOption(String prompt, String... options) {//这里这样写相当于把options当成一个数组
            while (true) {
                String input = readLine(prompt);
                for (String option : options) {
                    if (option.equalsIgnoreCase(input)) {
                        return option.toLowerCase();
                    }
                }
                System.out.printf("[提示] \"%s\" 不是有效选项，可用选项：%s%n",
                        input, String.join(" / ", options));//这里的join相当于stringjoiner，将字符串拼接在一起
            }
        }

        /** 读取是/否确认。 */
        public boolean confirm(String prompt) {
            String answer = readOption(prompt + " (y/n)：", "y", "n");
            return "y".equals(answer);//确认选择是或否
        }



        public void close() {
            scanner.close();
        }
    }
