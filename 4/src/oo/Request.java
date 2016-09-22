
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
		System.out.println("�����๤����");
		String FRstr="^\\(FR,([0-9]|1\\d|20),(UP|DOWN)\\)";//¥�����������ʽ��ʱ��Ϊdouble������
		String ERstr="^\\(ER,#[0-2],([0-9]|1\\d|20)\\)";//�������������ʽ��ʱ��Ϊdouble������
		Scanner reader=new Scanner(System.in);
		String request=new String();
		while (!(request=reader.nextLine()).contentEquals("run"))
		{
			System.out.println("���������");
synchronized (runro) {

	try {
		Thread.currentThread().sleep(10);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	//			System.out.println(request);
	if (!request.matches(FRstr) && !request.matches(ERstr)) {
		System.out.println("�����󲻷�������Ҫ��");
		continue;//��ʽ����ȷ�ô�������ԡ�
	} else {
		Long time = new Date().getTime();//��ǰʱ��
		String[] temp1 = new String[4];
		temp1 = request.split("[(,)]");
		if ((temp1[2].contentEquals("20") && temp1[3].contentEquals("UP"))
				|| (temp1[2].contentEquals("1") && temp1[3].contentEquals("DOWN"))) {
			continue;//20&UP����1&DOWN�ǲ����Եġ�
		}
		String[] temp = new String[5];
		for (int i = 0; i < temp1.length; i++) {
			temp[i] = temp1[i];
		}
		temp[temp1.length] = "3";//��ǣ�3��ʾδ��ĳ�����ݵ����Ӵ���0��1��2��ʾ����Ӧ���ݵ����Ӵ���
		strs[length++] = temp;//����������������������С�
		
		if (length == 1) {
			initialTime = new Date().getTime();//����һ����Ч�������ʱ����¼��ʼʱ�䡣
			ele[0].setinitialTime(initialTime);//���õ��ݵĳ�ʼʱ�䡣
		}
		dispatcher.setRequest(strs[length - 1]);//���µ����󴫸����������������з��䡣
	}
}
		}
		synchronized (runro) {//��
			System.out.println("�������");
			Request.runro = "false";
			Patcher.setrunro("false");
			ele[0].setrunro("false");//�������̶߳�������
			if (length == 0) {
				System.out.println("�������󣬲��ܽ��е��ݵ��ȡ�");
			} //��ȫ�����󲻷����﷨��ʽ���������
		}
	}
	
}