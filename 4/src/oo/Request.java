
package oo;

import java.util.Date;
import java.util.Scanner;

public class Request implements Runnable{
	private Patcher dispatcher;
	private int length=0;
	private Long initialTime;
	private RequestQ Q;
	private Elevator[] ele=new Elevator[3];
	private static String runro;
	private String[] strs[];
public Request(Elevator ele0,Elevator ele1,Elevator ele2,RequestQ Q,Patcher dispatcher,String runro) {
		super();
		this.ele[0] = ele0;
		this.ele[1]=ele1;
		this.ele[2]=ele2;
		strs=new String[1000][5];
		length=0;
		this.Q=Q;
		initialTime=(long) 0;
		this.dispatcher=dispatcher;
		Request.runro=runro;
	}
	public int getlength() {
		return length;
	}
	public void setlength(int length) {
		this.length = length;
	}
	public String[][] getStrs() {
		return strs;
	}
	public void setStrs(String[][] strs) {
		this.strs = strs;
	}
	
	public RequestQ getQ() {
		return Q;
	}
	public void setQ(RequestQ Q) {
		this.Q = Q;
	}
	public Long getinitialTime() {
		return initialTime;
	}
	public void setinitialTime(Long initialTime) {
		this.initialTime = initialTime;
	}
	
	public Patcher getDispatcher() {
		return dispatcher;
	}
	public void setDispatcher(Patcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	public String getrunro() {
		return runro;
	}
	public void setrunro(String runro) {
		Request.runro = runro;
	}
	@Override
	public void run() {
		System.out.println("请求类工作啦");
		String FRstr="^\\(FR,([0-9]|1\\d|20),(UP|DOWN)\\)";//楼层请求输入格式。时间为double型整数
		String ERstr="^\\(ER,#[0-2],([0-9]|1\\d|20)\\)";//电梯请求输入格式。时间为double型整数
		Scanner reader=new Scanner(System.in);
		String request=new String();
		while (!(request=reader.nextLine()).contentEquals("run"))
		{
			System.out.println("请继续输入");
synchronized (runro) {

	try {
		Thread.currentThread().sleep(10);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	//			System.out.println(request);
	if (!request.matches(FRstr) && !request.matches(ERstr)) {
		System.out.println("该请求不符合输入要求");
		continue;//格式不正确该次请求忽略。
	} else {
		Long time = new Date().getTime();//当前时间
		String[] temp1 = new String[4];
		temp1 = request.split("[(,)]");
		if ((temp1[2].contentEquals("20") && temp1[3].contentEquals("UP"))
				|| (temp1[2].contentEquals("1") && temp1[3].contentEquals("DOWN"))) {
			continue;//20&UP或者1&DOWN是不可以的。
		}
		String[] temp = new String[5];
		for (int i = 0; i < temp1.length; i++) {
			temp[i] = temp1[i];
		}
		temp[temp1.length] = "3";//标记，3表示未被某个电梯当作捎带，0，1，2表示被相应电梯当作捎带。
		strs[length++] = temp;//放入请求类所保存的数组中。
		
		if (length == 1) {
			initialTime = new Date().getTime();//当第一个有效请求产生时，记录初始时间。
			ele[0].setinitialTime(initialTime);//设置电梯的初始时间。
		}
		dispatcher.setRequest(strs[length - 1]);//将新的请求传给调度器，让它进行分配。
	}
}
		}
		synchronized (runro) {//锁
			System.out.println("输入结束");
			Request.runro = "false";
			Patcher.setrunro("false");
			ele[0].setrunro("false");//让所有线程都结束。
			if (length == 0) {
				System.out.println("输入有误，不能进行电梯调度。");
			} //若全部请求不符合语法格式，输出错误。
		}
	}
	
}