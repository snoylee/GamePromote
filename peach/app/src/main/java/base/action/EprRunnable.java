package base.action;

import java.util.List;

import base.action.Action.Param;
import base.action.asyc.BackRunnable;
import base.action.asyc.Compt;

public class EprRunnable extends BackRunnable{
	
	public AsynTask asyntask;

	public AsynTask getAsyntask() {
		return asyntask;
	}

	public EprRunnable setAsyntask(AsynTask asyntask) {
		this.asyntask = asyntask;
		return this;
	}

	@Override
	public void run() throws Exception {
		// TODO Auto-generated method stub
		asyntask.run();
	}

	
	
}
