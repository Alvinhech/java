
package oo;

public class RequestQ {
	private int front;
	private int rear;
	private String[][] Q;
//	front表示队列的对头的索引-1，rear表示队列的尾部的索引。
//	当front==rear的时候表示队列为空。
	public RequestQ(int n)
	{
		Q=new String[n][5];
		front=-1;
		rear=-1;
	}
	public void push(String[] str)
	{
		Q[++rear]=str;
	}//从请求类中将下一请求加入队列中。
	public void pop(int i)
	{
		for (int j=i;j>front+1;j--)
		{
			for (int k=0;k<Q[j].length;k++)
			{
				Q[j][k]=Q[j-1][k];
			}
		}
		front++;
	}//删除i位置的请求
	public String[] get(Elevator ele)
	{
		String Des=ele.getdir();
		if (Des.contentEquals("UP"))
		{
			int i=0;
			for (i=front+1;i<=rear;i++)
			{
				if (Q[i][1].contentEquals("ER")&&Integer.parseInt(Q[i][2])>ele.getFloor())
				{
					break;
				}
			}
			if (i==rear+1)
			{
				return Q[++front];
			}
			else
			{
				String[] que=Q[i];
				pop(i);
				return que;
			}
		}
		else if (Des.contentEquals("DOWN"))
		{
			int i=0;
			for (i=front+1;i<=rear;i++)
			{
				if (Q[i][1].contentEquals("ER")&&Integer.parseInt(Q[i][2])<ele.getFloor())
				{
					break;
				}
			}
			if (i==rear+1)
			{
				return Q[++front];
			}
			else
			{
				String[] que=Q[i];
				pop(i);
				return que;
			}
		}
		else
		{
			return Q[++front];
		}
		
	}//取出队头字符串数组。
	public boolean isempty()
	{
		return front==rear;
	}
	public String[][] getQ() {
		return Q;
	}
	public void setQ(String[][] Q) {
		this.Q = Q;
	}
	public int getFront() {
		return front;
	}
	public void setFront(int front) {
		this.front = front;
	}
	public int getRear() {
		return rear;
	}
	public void setRear(int rear) {
		this.rear = rear;
	}
}