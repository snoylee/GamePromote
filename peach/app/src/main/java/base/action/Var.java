package base.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.frame.DataUnit;
import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/11/1.
 */
public class Var extends Epr  {
    private String dflt;
    private String writeoption;
    private String raw;
    private List<String> names = new ArrayList<String>();
	private DataUnit unit;


    public Var(String raw,DataUnit unit) {
        this.raw = raw;
        this.unit = unit;
        parse();
    }

    public void parse() {
        String raw = this.raw;
        String text = raw.substring(2, raw.length() - 1);
        String[] rry;
        if (text.contains(":")) {
            rry = text.split(":");
            names = Arrays.asList(rry[0].split("\\|"));
            dflt = rry[1];
            if (rry.length > 2) {
                writeoption = rry[2];
            }
        } else {
            names.addAll(Arrays.asList(text.split("\\|")));
        }
    }

    public boolean hasDefault() {
        return dflt != null;
    }

    public List<String> getNames() {
        return names;
    }

    public String getDefault() {
        return dflt;
    }



    @Override
    public Object run() {
        for (String n : names) {
            if (unit.getRepo().containsKey(n)) {
                return unit.getRepo().get(n);
            }
        }
        if (hasDefault()) {
            return getDefault();
        }
        return "";

    }
}
