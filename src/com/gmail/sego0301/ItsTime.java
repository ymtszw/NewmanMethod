package com.gmail.sego0301;


import java.util.Calendar;

public class ItsTime {

	public ItsTime() {
		// TODO 閾ｪ蜍慕函謌舌＆繧後◆繧ｳ繝ｳ繧ｹ繝医Λ繧ｯ繧ｿ繝ｼ繝ｻ繧ｹ繧ｿ繝?
	}

	/**
	 * @param args
	 */
	public static void time() {

		Calendar c = Calendar.getInstance();
		int month=c.get(Calendar.MONTH)+1;
		String minute;
		if(c.get(Calendar.MINUTE)<10){
			minute="0"+String.valueOf(c.get(Calendar.MINUTE));
		}
		else{
			minute=String.valueOf(c.get(Calendar.MINUTE));

		}
		System.out.println(c.get(Calendar.YEAR)+"-"+
				month+"-"+c.get(Calendar.DAY_OF_MONTH) +" "+c.get(Calendar.HOUR_OF_DAY)
				+":"+minute+":"+c.get(Calendar.SECOND));


		// TODO 閾ｪ蜍慕函謌舌＆繧後◆繝｡繧ｽ繝?ラ繝ｻ繧ｹ繧ｿ繝?

	}



}
