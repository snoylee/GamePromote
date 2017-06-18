package base.action;

/**
 * Created by minhua on 2015/11/1.
 */
public class Param {
    public String pre;
    public String after;
    public Epr epr;

    public Param(String pre, Epr epr, String after) {
        this.pre = pre;
        this.after = after;
        this.epr = epr;
    }

    public String run() {
        return this.pre + epr.run() + this.after;
    }
}
