package oo;


import java.util.Date;

public class Elevator implements Runnable{
	static String runro; //锁
	private static RequestQ Q;//请求队列，这里存的都是还没有响应的请求，包括被替换的某个电梯的捎带请求。
	private int num;//电梯编号
	private int destination;//最近一次停止的楼层
	private int floor;//当前楼层
	private String dir;//当前状态时的方向
	private String[] sub_request;//当前捎带请求
	private String[] main_request;//当前主请求
	private long time;//当前时间
	private static long initialTime;//初始时间
	private long nexttime;//电梯运动到下一状态的时间：如果电梯是运动的，则下一状态为下一楼层，如果电梯静止，则下一状态为电梯开关门后。
	private int amount;//累计运动量
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
			this.sub_request[4]=""+num;//标记为某个电梯的捎带请求
		}
		
	}
	
	@Override
	public void run() {
		while (runro.contentEquals("true"))//表示还没有输入完毕，现在还可以运行
		{
			
			synchronized (Elevator.runro) {//锁
				//首先确定目的地和方向，这里目的地是最近一次要停止的楼层。
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
				}//获取主请求和捎带请求的楼层。
				if (sub_request != null) {
					if (Math.abs(subfloor)>Math.abs(mainfloor))
					{
						destination=mainfloor;
					}//这种情况在电梯请求为捎带请求的时候可能会出现
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
				}//目的地和楼层设置完毕。
				time = new Date().getTime();//设置当前时间
				//下面改变电梯状态。
				if (dir == "UP" || dir == "DOWN") {//电梯上一时刻在运动
					if (time >= nexttime) {//电梯应该转换为下一时刻的状态了
						if (dir == "UP")
						{
							floor ++;
						}
						else
						{
							floor --;
						}//改变当前楼层
						amount = amount + 1;//改变运动量
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
						//改变方向和下一状态时间
						Print_sta();//输出电梯当前状态
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
								//////////////打印捎带请求
								sub_request=null;//清空捎带请求
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
								System.out.println(")");/////////////////打印主请求
								main_request=null;//清空主请求
							}
							
						}
						//下面是打印队列中所有在该楼层可以完成的请求
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
									System.out.println(")");/////////////打印队列中请求
//									System.out.println("电梯"+num+"中删除队列1");
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
									System.out.println(")");//////////打印队列中请求
//									System.out.println("电梯"+num+"中删除队列2");
										synchronized (Q) {
											Q.pop(i);//////////////////////
										}
								}
							}
						}//打印完成
						
						//因为之前输出请求时清空了一些请求，现在应该查找能响应的主请求和捎带请求
						if (main_request==null)
						{//没有主请求
							if (sub_request!=null)
							{
								setMain_request(sub_request);
								nexttime=nexttime+3000;
								new_subrequest();//这种情况只出现在电梯请求为捎带请求，主请求先于捎带请求完成的情况，这时将捎带请求设为主请求。
							}
							else
							{//这时没有请求了，添加主请求和捎带请求。
								
								new_mainrequest();//添加主请求。可能为null。
								
								if (main_request!=null)//有主请求
								{
									new_subrequest();//添加捎带请求，但不一定有捎带请求，可能为null。
//									System.out.println("有主请求的电梯是："+num);
								}
							}
						}
						else
						{//有主请求
							nexttime=nexttime+3000;
							new_subrequest();//添加捎带请求。
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
				}//主请求楼层
				//这时主请求肯定不是null。
				//下面是查找最能先完成的捎带请求。
				for (int i = Q.getFront() + 1; i <= Q.getRear(); i++) {
					if (floor < mainfloor) {
						if ((Q.getQ())[i][1].contentEquals("FR")) {//楼层请求
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
						} else {//电梯请求
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
					//如果没找到
					setSub_request(null);
				} else {//找到捎带请求
					setSub_request((Q.getQ())[k]);
					synchronized (Q) {
						Q.pop(k);///////////////////////////////删除队列中的该请求
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
					{//找到了一个未被别的电梯标记过的请求
						nexttime = new Date().getTime() + 3000;
						setMain_request((Q.getQ())[Q.getFront() + 1]);//设置主请求
						Q.pop(Q.getFront() + 1);//////////////////////删除队列中的该请求
						break;
					}
					else if (i==Q.getRear())
					{
						setMain_request(null);//整个队列未找到可用的请求。
					}
				}
				
			} else {//队列为空，找不到请求作为主请求。
				setMain_request(null);
			}
		}
	}
	public void Print_sta()
	{
		System.out.println("(#"+num+",#"+floor+","+amount+","+Math.rint((time-initialTime) / 100) / 10+")");
	}
	
}


