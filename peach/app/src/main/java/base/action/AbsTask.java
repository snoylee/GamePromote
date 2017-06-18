package base.action;

import java.util.List;

import base.action.Action.Param;

public abstract class AbsTask {
	public Action thisAction;

	public Action getThisAction() {
		return thisAction;
	}

	public void setThisAction(Action thisAction) {
		this.thisAction = thisAction;
	}

	public abstract void back(String methodname, List<String> params, Param param);
	protected abstract Object invoke(String methodname, List<String> params, Param param);
	
	public abstract Object run(String methodname, List<String> params, Param param);
	
	private String methodname;
	private List<String> params;
	private Param param;
	
    public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public List<String> getParams() {
		return params;
	}
	public void setParams(List<String> params) {
		this.params = params;
	}
	public Param getParam() {
		return param;
	}
	public void setParam(Param param) {
		this.param = param;
	}
	public Object run() {
		// TODO Auto-generated method stub
		return run(methodname,params,param);
	}

}
