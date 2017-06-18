package base.action.task;

import java.util.List;

import android.content.Intent;
import base.action.Action.Param;
import base.action.Task;

public class StartActivity extends Task {

	@Override
	public Object run(String methodname, List<String> params, Param param) {
		// TODO Auto-generated method stub
		try {
			if(params.size()>1){
				Class clazz = Class.forName(params.get(1));
				Intent intent = new Intent(param.getActivity(),clazz);

				intent.putExtra("layout", params.get(0));
				param.getActivity().startActivity(intent);
			}else{
				Class clazz = Class.forName(params.get(0));
				Intent intent = new Intent(param.getActivity(),clazz);

				param.getActivity().startActivity(intent);
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.run(methodname, params, param);
	}
	
	
}
