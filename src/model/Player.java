package model;

public class Player extends Participant{
    private boolean hitDecision = false;//用private进行封装，防止外界访问
    public Player(String name, int chips) {
        super(name, chips);
    }
    public void setHitDecision(boolean hitDecision) {
        this.hitDecision = hitDecision;
    }//将调用者的hitdecison进行修改
    @Override
    public boolean wantsHit() {
        return hitDecision;
    }//判断是否继续加注
}
