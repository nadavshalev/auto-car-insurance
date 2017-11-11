package com.nadav.shalev.outocar8;

public class ReadText {

	public static String Message;
	public static String Time;
	public static String set;
	public static void ReadText(String message) {
		// TODO Auto-generated method stub

		//String Mstart  = "בקשתך התקבלה. כיסוי הביטוח בתוקף עד להודעת סגירה. יתרת השעות: 9:01";
		//String M2start = "שלחת הודעת פתיחה פעמיים. לבירור פנה למוקד בטלפון *3455";
		//String Mend = "הודעת הסגירה נתקבלה. הכיסוי הביטוחי הסתיים מרגע זה. יתרת השעות: 6:40";
		//String M2end = "שלחת הודעת סגירה וטרם נרשמה פתיחת נסיעה. לבירור פנה למוקד בטלפון *3455";
		//String mess = "הפניקס צעיר, תזכורת! נסיעתך טרם נסגרה נכון ל 18:23 לעזרה פנה ל *3455";
		
		Message = message;
		char FirstLetter = Message.charAt(1);
		System.out.println(Message.length());
		switch(FirstLetter){
		case 'ק':
			set = "on";
			break;
		case 'ו':
			set = "off";
			ReadTime();
			break;
		case 'ל':
			char diff = Message.charAt(11);
			switch(diff){
			case 'פ':
				set = "on";
				break;
			case 'ס':
				set = "off";
				break;
			}
			break;
		case 'פ':
			break;
		default: {
		    System.out.println("Else");
		    set = "nothing";
		}

		}
	}
	
	public static void ReadTime(){
		char[] FindTime;
		Time = null;
		if(Message.length()==68){
			FindTime = new char[5];
			FindTime[0] = Message.charAt(63);
			FindTime[1] = Message.charAt(64);
			FindTime[2] = Message.charAt(65);
			FindTime[3] = Message.charAt(66);
			FindTime[4] = Message.charAt(67);
			Time = new String(FindTime);
		}else if(Message.length() ==69){
			FindTime = new char[6];
			FindTime[0] = Message.charAt(63);
			FindTime[1] = Message.charAt(64);
			FindTime[2] = Message.charAt(65);
			FindTime[3] = Message.charAt(66);
			FindTime[4] = Message.charAt(67);
			FindTime[5] = Message.charAt(68);
			Time = new String(FindTime);
		}	
	}

}

