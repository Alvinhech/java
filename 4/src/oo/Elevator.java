package oo;


import java.util.Date;

public class Elevator implements Runnable{
	static String runro; //��
	private static RequestQ Q;//������У������Ķ��ǻ�û����Ӧ�����󣬰������滻��ĳ�����ݵ��Ӵ�����
	private int num;//���ݱ��
	private int destination;//���һ��ֹͣ��¥��
	private int floor;//��ǰ¥��
	private String dir;//��ǰ״̬ʱ�ķ���
	private String[] sub_request;//��ǰ�Ӵ�����
	private String[] main_request;//��ǰ������
	private long time;//��ǰʱ��
	private static long initialTime;//��ʼʱ��
	private long nexttime;//�����˶�����һ״̬��ʱ�䣺����������˶��ģ�����һ״̬Ϊ��һ¥�㣬������ݾ�ֹ������һ״̬Ϊ���ݿ����ź�
	private int amount;//�ۼ��˶���
	public Elevator(int num,RequestQ Q,String runro)
	{
		floor=1;
		destination=1;
		dir="STOP";
		this.num=num;
		nexttime=time;
		sub_request=null;
		main_request=null;
		amount=0;
		time=new Date().getTime();
		initialTime=time;
		Elevator.runro=runro;
		Elevator.Q=Q;
	}
	public void setinitialTime(Long initialTime) {
		Elevator.initialTime = initialTime;
	}
	
	public Long getinitialTime() {
		return initialTime;
	}


	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}

	public String getrunro() {
		return runro;
	}

	public void setrunro(String runro) {
		Elevator.runro = runro;
	}
	public long getnexttime() {
		return nexttime;
	}
	public void setnexttime(long nexttime) {
		this.nexttime = nexttime;
	}
	
	public RequestQ getQ() {
		return Q;
	}
	public void setQ(RequestQ Q) {
		Elevator.Q = Q;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public int getamount() {
		return amount;
	}
	public void setamount(int amount) {
		this.amount = amount;
	}
	public String getdir() {
		return dir;
	}
	public void setdir(String dir) {
		this.dir = dir;
	}
	public String[] getMain_request() {
		return main_request;
	}
	public void setMain_request(String[] main_request) {
		this.main_request = main_request;
	}
	
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	public int genexttimeum() {
		return num;
	}
	public void senexttimeum(int num) {
		this.num = num;
	}
	public String[] getSub_request() {
		return sub_request;
	}
	public void setSub_request(String[] sub_request) {
		this.sub_request = sub_request;
		if (sub_request!=null)
		{
			this.sub_request[4]=""+num;//���Ϊĳ�����ݵ��Ӵ�����
		}
		
	}
	
	@Override
	public void run() {
		while (runro.contentEquals("true"))//��ʾ��û��������ϣ����ڻ���������
		{
			
			synchronized (Elevator.runro) {//��
				//����ȷ��Ŀ�ĵغͷ�������Ŀ�ĵ������һ��Ҫֹͣ��¥�㡣
				int subfloor=0;
				int mainfloor=0;
				if (sub_request!=null)
				{
					if (sub_request[1].contentEquals("FR"))
					{
						subfloor=Integer.parseInt(sub_request[2]);
					}
					else
					{
						subfloor=Integer.parseInt(sub_request[3]);
					}
				}
				if (main_request!=null)
				{
					if (main_request[1].contentEquals("FR"))
					{
						mainfloor=Integer.parseInt(main_request[2]);
					}
					else
					{
						mainfloor=Integer.parseInt(main_request[3]);
					}
				}//��ȡ��������Ӵ������¥�㡣
				if (sub_request != null) {
					if (Math.abs(subfloor)>Math.abs(mainfloor))
					{
						destination=mainfloor;
					}//��������ڵ�������Ϊ�Ӵ������ʱ����ܻ����
					else
					{
						destination=subfloor;
					}
					if (destination > floor) {
						dir = "UP";
					} else if (destination < floor) {
						dir = "DOWN";
					} else {
						dir = "STOP";
					}
				} else if (main_request != null) {
					destination=mainfloor;
					if (destination > floor) {
						dir = "UP";
					} else if (destination < floor) {
						dir = "DOWN";
					} else {
						dir = "STOP";
					}
				} else {
					destination = floor;
					dir = "STOP";
				}//Ŀ�ĵغ�¥��������ϡ�
				time = new Date().getTime();//���õ�ǰʱ��
				//����ı����״̬��
				if (dir == "UP" || dir == "DOWN") {//������һʱ�����˶�
					if (time >= nexttime) {//����Ӧ��ת��Ϊ��һʱ�̵�״̬��
						if (dir == "UP")
						{
							floor ++;
						}
						else
						{
							floor --;
						}//�ı䵱ǰ¥��
						amount = amount + 1;//�ı��˶���
						if (destination > floor) {
							dir = "UP";
							nexttime = nexttime + 3000;
						} else if (destination < floor) {
							dir = "DOWN";
							nexttime = nexttime + 3000;
						} else {         
							dir = "STOP";
							nexttime = nexttime + 6000;
						}
						//�ı䷽�����һ״̬ʱ��
						Print_sta();//������ݵ�ǰ״̬
					}
				} else if (dir == "STOP") {
					if (time >= nexttime) {
						
						if (floor==subfloor)
						{
							if(sub_request!=null)
							{
								System.out.print("(");
								for (int i=1;i<sub_request.length-1;i++)
								{
									System.out.print(sub_request[i]);
									System.out.print(",");
								}
								System.out.print(Math.rint((time-initialTime) / 100) / 10);
								System.out.print(",#");
								System.out.print(num);
								System.out.println(")");
								//////////////��ӡ�Ӵ�����
								sub_request=null;//����Ӵ�����
							}
							
						}
						if (floor==mainfloor)
						{
							if(main_request!=null)
							{
								System.out.print("(");
								for (int i=1;i<main_request.length-1;i++)
								{
									System.out.print(main_request[i]);
									System.out.print(",");
								}
								System.out.print(Math.rint((time-initialTime) / 100) / 10);
								System.out.print(",#");
								System.out.print(num);
								System.out.println(")");/////////////////��ӡ������
								main_request=null;//���������
							}
							
						}
						//�����Ǵ�ӡ�����������ڸ�¥�������ɵ�����
						for (int i=Q.getFront()+1;i<Q.getRear();i++)
						{
							if ((Q.getQ())[i][1].contentEquals("FR"))
							{
								if (floor==Integer.parseInt((Q.getQ())[i][2]))
								{
									System.out.print("(");
									for (int i1=1;i1<(Q.getQ())[i].length-1;i1++)
									{
										System.out.print((Q.getQ())[i][i1]);
										System.out.print(",");
									}
									System.out.print(Math.rint((time-initialTime) / 100) / 10);
									System.out.print(",#");
									System.out.print(num);
									System.out.println(")");/////////////��ӡ����������
//									System.out.println("����"+num+"��ɾ������1");
										synchronized (Q) {
											Q.pop(i);///////////////////////
										}
								}
							}
							else
							{
								if (floor==Integer.parseInt((Q.getQ())[i][3]))
								{
									System.out.print("(");
									for (int i1=1;i1<(Q.getQ())[i].length-1;i1++)
									{
										System.out.print((Q.getQ())[i][i1]);
										System.out.print(",");
									}
									System.out.print(Math.rint((time-initialTime) / 100) / 10);
									System.out.print(",#");
									System.out.print(num);
									System.out.println(")");//////////��ӡ����������
//									System.out.println("����"+num+"��ɾ������2");
										synchronized (Q) {
											Q.pop(i);//////////////////////
										}
								}
							}
						}//��ӡ���
						
						//��Ϊ֮ǰ�������ʱ�����һЩ��������Ӧ�ò�������Ӧ����������Ӵ�����
						if (main_request==null)
						{//û��������
							if (sub_request!=null)
							{
								setMain_request(sub_request);
								nexttime=nexttime+3000;
								new_subrequest();//�������ֻ�����ڵ�������Ϊ�Ӵ����������������Ӵ�������ɵ��������ʱ���Ӵ�������Ϊ������
							}
							else
							{//��ʱû�������ˣ������������Ӵ�����
								
								new_mainrequest();//��������󡣿���Ϊnull��
								
								if (main_request!=null)//��������
								{
									new_subrequest();//����Ӵ����󣬵���һ�����Ӵ����󣬿���Ϊnull��
//									System.out.println("��������ĵ����ǣ�"+num);
								}
							}
						}
						else
						{//��������
							nexttime=nexttime+3000;
							new_subrequest();//����Ӵ�����
						}
					}
				}
			}
		}
	}
	public void new_subrequest()
	{
				int min = 20;
				int k = -1;
				int mainfloor = 0;
				if (main_request[1].contentEquals("FR")) {
					mainfloor = Integer.parseInt(main_request[2]);
				} else {
					mainfloor = Integer.parseInt(main_request[3]);
				}//������¥��
				//��ʱ������϶�����null��
				//�����ǲ�����������ɵ��Ӵ�����
				for (int i = Q.getFront() + 1; i <= Q.getRear(); i++) {
					if (floor < mainfloor) {
						if ((Q.getQ())[i][1].contentEquals("FR")) {//¥������
							if ((Q.getQ())[i][3].contentEquals("UP")
									&& Integer.parseInt((Q.getQ())[i][2]) > floor
									&& Integer.parseInt((Q.getQ())[i][2]) <= mainfloor) {
								if (Math.abs(Integer.parseInt((Q.getQ())[i][2]) - floor) < min) {
									if ((Q.getQ())[i][4].contentEquals("3")||(Q.getQ())[i][4].contentEquals(""+num))
									{
										k = i;
										min = Math.abs(Integer.parseInt((Q.getQ())[i][2]) - floor);
									}
									
								}
							}
						} else {//��������
							if (Integer.parseInt((Q.getQ())[i][3]) > floor) {
								if (Math.abs(Integer.parseInt((Q.getQ())[i][3]) - floor) < min) {
									if ((Q.getQ())[i][4].contentEquals("3")||(Q.getQ())[i][4].contentEquals(""+num))
									{
										k = i;
										min = Math.abs(Integer.parseInt((Q.getQ())[i][3]) - floor);
									}
								}
							}
						}

					} else if (floor > mainfloor) {
						if ((Q.getQ())[i][1].contentEquals("FR")) {
							if ((Q.getQ())[i][3].contentEquals("DOWN")
									&& Integer.parseInt((Q.getQ())[i][2]) < floor
									&& Integer.parseInt((Q.getQ())[i][2]) >= mainfloor) {
								if (Math.abs(Integer.parseInt((Q.getQ())[i][2]) - floor) < min) {
									if ((Q.getQ())[i][4].contentEquals("3")||(Q.getQ())[i][4].contentEquals(""+num))
									{
										k = i;
										min = Math.abs(Integer.parseInt((Q.getQ())[i][2]) - floor);
									}
								}
							}
						} else {
							if (Integer.parseInt((Q.getQ())[i][3]) < floor) {
								if (Math.abs(Integer.parseInt((Q.getQ())[i][3]) - floor) < min) {
									if ((Q.getQ())[i][4].contentEquals("3")||(Q.getQ())[i][4].contentEquals(""+num))
									{
										k = i;
										min = Math.abs(Integer.parseInt((Q.getQ())[i][3]) - floor);
									}
								}
							}
						}
					}
				}
				if (k == -1) {
					//���û�ҵ�
					setSub_request(null);
				} else {//�ҵ��Ӵ�����
					setSub_request((Q.getQ())[k]);
					synchronized (Q) {
						Q.pop(k);///////////////////////////////ɾ�������еĸ�����
					}
				}
	}
	public void new_mainrequest()
	{
		synchronized (Q) {
			if (!Q.isempty()) {
				for (int i=Q.getFront()+1;i<=Q.getRear();i++)
				{
					if ((Q.getQ())[i][4].contentEquals("3")||(Q.getQ())[i][4].contentEquals(""+num))
					{//�ҵ���һ��δ����ĵ��ݱ�ǹ�������
						nexttime = new Date().getTime() + 3000;
						setMain_request((Q.getQ())[Q.getFront() + 1]);//����������
						Q.pop(Q.getFront() + 1);//////////////////////ɾ�������еĸ�����
						break;
					}
					else if (i==Q.getRear())
					{
						setMain_request(null);//��������δ�ҵ����õ�����
					}
				}
				
			} else {//����Ϊ�գ��Ҳ���������Ϊ������
				setMain_request(null);
			}
		}
	}
	public void Print_sta()
	{
		System.out.println("(#"+num+",#"+floor+","+amount+","+Math.rint((time-initialTime) / 100) / 10+")");
	}
	
}


