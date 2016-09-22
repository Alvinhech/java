
package oo;

import java.util.Date;

public class Patcher implements Runnable{
	private static RequestQ Q;
	private Elevator[] ele=new Elevator[3];
	private String[] request;
	private static String runro;
	public Patcher() {
		super();
	}
	
	public Patcher(Elevator ele0,Elevator ele1,Elevator ele2, RequestQ Q, String[] request,String runro) {
		super();
		this.ele[0] = ele0;
		this.ele[1]=ele1;
		this.ele[2]=ele2;
		Patcher.Q = Q;
		this.request = request;
		Patcher.runro=runro;
	}

	public static String getrunro() {
		return runro;
	}

	public static void setrunro(String runro) {
		Patcher.runro = runro;
	}

	public RequestQ getQ() {
		return Q;
	}
	public void setQ(RequestQ Q) {
		Patcher.Q = Q;
	}
	public Elevator[] getEle() {
		return ele;
	}
	public void setEle(Elevator[] ele) {
		this.ele = ele;
	}
	public String[] getRequest() {
		return request;
	}
	public void setRequest(String[] request) {
		this.request = request;
	}
	


	public int compare(int[] A1, int[] A2,int[] A3)
	{
		if (A1[0]<A2[0])
		{
			if (A2[0]<A3[0])
			{
				return 2;
			}
			else if (A2[0]==A3[0])
			{
				if (A2[1]<=A3[1])
				{
					return 1;
				}
				else
				{
					return 2;
				}
			}
			else
			{
				return 1;
			}
		}
		else if (A1[0]==A2[0])
		{
			if (A2[0]<A3[0])
			{
				return 2;
			}
			else if (A2[0]>A3[0])
			{
				if (A1[1]<=A2[1])
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
			else
			{
				if (A1[1]<=A2[1])
				{
					if (A1[1]<=A3[1])
					{
						return 0;
					}
					else
					{
						return 2;
					}
				}
				else
				{
					if (A2[1]<=A3[1])
					{
						return 1;
					}
					else
					{
						return 2;
					}
				}
			}
		}
		else
		{
			if (A1[0]<A3[0])
			{
				return 2;
			}
			else if (A1[0]==A3[0])
			{
				if (A1[1]<=A3[1])
				{
					return 0;
				}
				else
				{
					return 2;
				}
			}
			else
			{
				return 0;
			}
		}
	}
	public void change(int num,int signal)
	{
			if (signal == 1) {
				ele[num].setMain_request(request);
				ele[num].setnexttime(new Date().getTime() + 3000);
			} else if (signal == 2) {
				if (ele[num].getSub_request()==null)
				{
					ele[num].setSub_request(request);
				}
				else
				{
					synchronized (Q) {
						String[] temp = new String[5];
						temp = ele[num].getSub_request();
						ele[num].setSub_request(request);
						if (!Q.isempty())
						{
							for (int i = Q.getFront() + 1; i <= Q.getRear(); i++) {
								if (Long.parseLong(temp[4]) < Long.parseLong((Q.getQ())[i][4])) {
									for (int j = Q.getRear(); j >= i; j--) {
										(Q.getQ())[j + 1] = (Q.getQ())[j];
									}
									(Q.getQ())[i] = temp;
									break;
								} else if (i == Q.getRear()) {
									(Q.getQ())[Q.getRear()+1] = temp;
									Q.setRear(Q.getRear()+1);
								}
							} 
						}
						else
						{
							Q.push(temp);
						}
						
					}
					
				}
			} 
			else
			{
				Q.push(request);
			}
	}


	
	public void run() {
		while (runro.contentEquals("true"))
		{
			synchronized (Elevator.runro) {
				if (request != null) {
					int[][] A = new int[3][2];
					int i;
					if (request[1].contentEquals("FR"))
					{
						A[0] = judge(ele[0]);
						A[1] = judge(ele[1]);
						A[2] = judge(ele[2]);
						
						i = compare(A[0], A[1], A[2]);
						
						change(i, A[i][0]);
					}
					else
					{
						i=request[2].charAt(1)-'0';
						A[i]=judge(ele[i]);
					}
					request = null;
				}
			}
		}
	}
	
	
	

	public int[] judge(Elevator ele)
	{
			String[] temp = request;
			int[] A = new int[2];
			if (ele.getMain_request() != null && ele.getdir() != "STOP") 
			{
				if (ele.getdir() == "UP") {
					if (temp[1].contentEquals("FR")) {
						if (ele.getFloor() < Integer.parseInt(temp[2])
								&& Integer.parseInt(temp[2]) <= ele.getDestination()
								&& (ele.getdir()).contentEquals(temp[3])) {
							A[0] = 2;
							A[1] = ele.getamount();
							return A;
						} else {
							A[0] = 0;
							A[1] = ele.getamount();
							return A;
						}
					} else {
						synchronized (Q) {
							if (ele.getFloor() < Integer.parseInt(temp[3])) {
								if (ele.getSub_request()==null)
								{
									ele.setSub_request(request);
								}
								else
								{
									if (Integer.parseInt(temp[3])<=ele.getDestination())
									{
										String[] tempp = new String[5];
										tempp = ele.getSub_request();
										ele.setSub_request(request);
										
										if (!Q.isempty())
										{
											for (int i = Q.getFront() + 1; i <= Q.getRear(); i++) {
												if (Long.parseLong(tempp[4]) < Long.parseLong((Q.getQ())[i][4])) {
													for (int j = Q.getRear(); j >= i; j--) {
														(Q.getQ())[j + 1] = (Q.getQ())[j];
													}
													(Q.getQ())[i] = tempp;
													break;
												} else if (i == Q.getRear()) {
													(Q.getQ())[Q.getRear()+1] = tempp;
													Q.setRear(Q.getRear()+1);
												}
											} 
										}
										else
										{
											Q.push(tempp);
										}
									}
								}
							}
						}
						return A;
					}
				} else if (ele.getdir() == "DOWN") {
					if (temp[1].contentEquals("FR")) {
						if (ele.getFloor() > Integer.parseInt(temp[2])
								&& Integer.parseInt(temp[2]) >= ele.getDestination()
								&& (ele.getdir()).contentEquals(temp[3])) {
							A[0] = 2;
							A[1] = ele.getamount();
							return A;
						} else {
							A[0] = 0;
							A[1] = ele.getamount();
							return A;
						}
					} else {
						synchronized (Q) {
							if (ele.getFloor() > Integer.parseInt(temp[3])) {
								if (ele.getSub_request()==null)
								{
									ele.setSub_request(request);
								}
								else
								{
									if (Integer.parseInt(temp[3])>=ele.getDestination())
									{
										String[] tempp = new String[5];
										tempp = ele.getSub_request();
										ele.setSub_request(request);
										if (!Q.isempty())
										{
											for (int i = Q.getFront() + 1; i <= Q.getRear(); i++) {
												if (Long.parseLong(tempp[4]) < Long.parseLong((Q.getQ())[i][4])) {
													for (int j = Q.getRear(); j >= i; j--) {
														(Q.getQ())[j + 1] = (Q.getQ())[j];
													}
													(Q.getQ())[i] = tempp;
													break;
												} else if (i == Q.getRear()) {
													(Q.getQ())[Q.getRear()+1] = tempp;
													Q.setRear(Q.getRear()+1);
												}
											} 
										}
										else
										{
											Q.push(tempp);
										}
									}
									
								}
								
							}
						}
						return A;
					}

				}
			}
			else if (ele.getdir() == "STOP" && ele.getMain_request() != null) {
				A[0] = 0;
				A[1] = ele.getamount();
				return A;
			} else {
				if (temp[1].contentEquals("FR")) {
					A[0] = 1;
					A[1] = ele.getamount();
					return A;
				} else {
					ele.setMain_request(temp);
					ele.setnexttime(new Date().getTime()+3000);
				}

			}
			return A;
		
	}
	
}