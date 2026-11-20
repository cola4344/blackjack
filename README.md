# 控制台人机对战 21 点（Blackjack）

## 一、项目名称与选题

- **项目名称**：控制台人机对战 21 点
- **选题**：候选项目五 · 控制台人机对战 21 点游戏（Blackjack）
- **运行环境**：标准控制台（Console / Terminal），JDK 21
- **开发语言**：Java（纯 JDK，无第三方依赖）

### 已实现的需求

**基础需求（全部完成）**

- [x] 面向对象封装 `Card`（4 花色 × 13 点数）与 `Shoe` 牌靴
- [x] 开局洗牌机制（`Collections.shuffle`）
- [x] 点数计算：2~10 按牌面，J/Q/K 计 10 点
- [x] **A 动态点数**：先算 11 点，总和 > 21 时自动降为 1 点，支持多张 A
- [x] 开局各发两张，庄家 1 张明牌 + 1 张暗牌
- [x] 玩家回合自由 Hit / Stand，爆牌即刻判负
- [x] 庄家回合揭开暗牌，< 17 强制要牌，>= 17 强制停牌
- [x] 结算：点数大者胜，相同点数 Push 退还筹码
- [x] 初始筹码与下注上限校验，筹码输光提示重新开局

**进阶加分项（已完成）**

- [x] 双倍下注（Double Down）：首轮发牌后仅可再要一张并翻倍下注
- [x] 4 副牌混洗 + 发牌过半自动切牌重洗

## 二、系统架构与主要类的职责

### 包结构

blackjack/
├── Main.java                    程序入口，只做组装与兜底
├── model/                       ← 数据实体（"是什么"）
│   ├── Suit.java                花色枚举
│   ├── Rank.java                点数枚举（含基础点数）
│   ├── Card.java                一张牌（不可变）
│   ├── Hand.java                ★ 一手牌，A 动态点数算法的所在地
│   ├── Shoe.java                牌靴（多副牌 + 洗牌 + 发牌）
│   ├── Participant.java         参与者抽象类（名字/手牌/筹码）
│   ├── Player.java              玩家（继承 Participant）
│   └── Dealer.java              庄家（继承 Participant，实现 <17 要牌规则）
├── game/                        ← 核心业务逻辑（"怎么玩"）
│   ├── Outcome.java             结算结果枚举（含赔率）
│   ├── Settler.java             结算器：判胜负 + 算返还
│   └── BlackjackGame.java       ★ 流程编排：下注→发牌→玩家→庄家→结算
├── view/                        ← 控制台输入输出（"怎么显示"）
│   └── ConsoleView.java         渲染器，只打印不改数据
└── util/                        ← 工具辅助
    └── ConsoleIO.java           ★ 健壮输入：非法输入拦截 + 引导重输

### 主要类职责

| 包                | 类              | 职责                                                        |
| ----------------- | --------------- | ----------------------------------------------------------- |
| `blackjack`       | `Main`          | 程序入口，仅负责组装依赖、启动游戏、顶层异常兜底            |
| `blackjack.model` | `Suit` / `Rank` | 花色与点数枚举，把显示文本和基础点数作为数据绑定            |
| `blackjack.model` | `Card`          | 一张不可变的扑克牌，重写 `equals`/`hashCode` 支持按内容比较 |
| `blackjack.model` | `Hand`          | 一手牌。**A 动态点数算法的唯一实现处**                      |
| `blackjack.model` | `Shoe`          | 牌靴：多副牌填充、洗牌、发牌、过半判定与重洗                |
| `blackjack.model` | `Participant`   | 参与者抽象基类：名字、手牌、筹码；声明抽象方法 `wantsHit()` |
| `blackjack.model` | `Player`        | 玩家，按外部传入的决策决定是否要牌                          |
| `blackjack.model` | `Dealer`        | 庄家，实现"< 17 要牌，>= 17 停牌"规则                       |
| `blackjack.game`  | `Outcome`       | 结算结果枚举，把提示文案与赔率作为数据绑定                  |
| `blackjack.game`  | `Settler`       | 纯函数结算器：判定胜负 + 计算返还筹码                       |
| `blackjack.game`  | `BlackjackGame` | 流程编排：下注 → 发牌 → 玩家回合 → 庄家回合 → 结算          |
| `blackjack.view`  | `ConsoleView`   | 控制台渲染，只打印不修改数据                                |
| `blackjack.util`  | `ConsoleIO`     | 健壮输入：非法输入拦截 + 友好提示 + 引导重输                |

### 关键设计决策

1. **A 不存储动态值，每次 `getValue()` 重算**
   因为 A 算 11 还是 1 取决于整手牌，存起来就必须在每次加牌后同步维护，容易产生不一致。
   算法：先把所有 A 按 11 累加并统计 A 的张数，再用
   `while (total > 21 && aceCount > 0) { total -= 10; aceCount--; }` 逐张降值。

2. **用枚举而不是字符串常量表示花色/点数**
   编译期即可发现拼写错误；枚举比较用 `==` 更安全（与 `String` 必须用 `equals` 形成对比）。
   同时把"显示文本"和"基础点数"作为枚举属性绑定，消除散落的 `switch` 分支。

3. **`Participant` 用抽象类而非接口**
   玩家与庄家共享"名字 + 手牌 + 筹码"的状态与实现，属于"是什么"关系。

4. **玩家/庄家差异抽象为 `wantsHit()`**
   主流程只需 `while (dealer.wantsHit())`，不需要任何"这是不是庄家"的判断，是多态的典型应用。

5. **结算逻辑独立为 `Settler` 纯函数工具类**
   不读写外部状态、不打印，因此可以脱离控制台直接单元测试（见 `SelfTest`）。

6. **输入与视图分离，且完全不使用 `Scanner.nextInt()`**
   `nextInt()` 遇到字母会抛 `InputMismatchException`，且与 `nextLine()` 混用会造成"回车残留"。
   统一改为 `nextLine() + Integer.parseInt()` 并用 `try/catch` 拦截，配合 `while(true)` 支持无限重输。

7. **`Random` 从外部注入**
   便于用固定种子复现测试结果；`model` 包完全不依赖控制台，将来替换为 GUI 无需改动模型层。

## 三、编译与运行指南

### 环境要求

- JDK 17 或以上（开发环境 JDK 21.0.12.1）
- 无需任何第三方依赖

### 编译

在项目根目录执行（Windows PowerShell）：

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src).FullName