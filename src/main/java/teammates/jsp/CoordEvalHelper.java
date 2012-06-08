package teammates.jsp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import teammates.datatransfer.CourseData;
import teammates.datatransfer.EvaluationData;

public class CoordEvalHelper extends Helper{
	// Specific parameters
	public String coordID;
	public CourseData[] courses;
	public EvaluationData submittedEval;
	public ArrayList<EvaluationData> evaluations;
	
	public CoordEvalHelper(Helper helper){
		super(helper);
	}
	
	/**
	 * Returns the timezone options as HTML code.
	 * None is selected, since the selection should only be done in client side.
	 * @return
	 */
	public ArrayList<String> getTimeZoneOptions(){
		double[] options = new double[]{-12,-11,-10,-9,-8,-7,-6,-5,-4.5,-4,-3.5,
										-3,-2,-1,0,1,2,3,3.5,4,4.5,5,5.5,5.75,6,
										7,8,9,10,11,12,13};
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i<options.length; i++){
			String temp = "UTC";
			if(options[i]!=0){
				if((int)options[i]==options[i])
					temp+=String.format(" %+03d:00", (int)options[i]);
				else
					temp+=String.format(" %+03d:%02d", (int)options[i],
							(int)(Math.abs(options[i]-(int)options[i])*300/5));
			}
			result.add("<option value=\""+format(options[i])+"\"" +
						(submittedEval!=null && submittedEval.timeZone==options[i]
							? "selected=\"selected\""
							: "") +
						">"+temp+"</option>");
		}
		return result;
	}
	
	/**
	 * Returns the grace period options as HTML code
	 * @return
	 */
	public ArrayList<String> getGracePeriodOptions(){
		ArrayList<String> result = new ArrayList<String>();
		for(int i=5; i<=30; i+=5){
			result.add("<option value=\""+i+"\"" +
						(submittedEval!=null && submittedEval.gracePeriod==i
							? " selected=\"selected\""
							: "") +
						">" + i+" mins</option>");
		}
		return result;
	}
	
	/**
	 * Returns the time options as HTML code
	 * By default the selected one is the last one.
	 * @param selectCurrentTime
	 * @return
	 */
	public ArrayList<String> getTimeOptions(boolean isStartTime){
		ArrayList<String> result = new ArrayList<String>();
		for(int i=1; i<=24; i++){
			result.add("<option value=\""+i+"\"" +
						(checkTimeSelected(i,isStartTime)
							? " selected=\"selected\""
							: "") +
						">" +
					   String.format("%04dH", i*100 - (i==24 ? 41 : 0)) +
					   "</option>");
		}
		return result;
	}
	
	public ArrayList<String> getCourseIDOptions(){
		ArrayList<String> result = new ArrayList<String>();
		for(CourseData course: courses){
			result.add("<option value=\"" + course.id + "\"" +
						(submittedEval!=null && course.id==submittedEval.course
							? " selected=\"selected\""
							: "" ) +
						">"+course.id+"</option>");
		}
		return result;
	}
	
	/**
	 * Helper to print the value of timezone the same as what javascript would
	 * produce.
	 * @param num
	 * @return
	 */
	private static String format(double num){
		if((int)num==num) return ""+(int)num;
		return ""+num;
	}
	
	private boolean checkTimeSelected(int hour, boolean isStart){
		if(submittedEval!=null){
			Date time = (isStart ? submittedEval.startTime : submittedEval.endTime);
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(time);
			if(cal.get(Calendar.MINUTE)==0){
				if(cal.get(Calendar.HOUR_OF_DAY)==hour) return true;
			} else {
				if(hour==24) return true;
			}
		} else {
			if(hour==24) return true;
		}
		return false;
	}
	
	public static void main(String[] args){
		for(String opt: new CoordEvalHelper(new Helper()).getTimeZoneOptions()){
			System.out.println(opt);
		}
	}
}
