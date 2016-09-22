package oo;

public class ThreadScheduleElevator {
	public static void main(String[] args) {
		String runro="true";
		RequestQ que=new RequestQ(1000);
		Elevator ele0=new Elevator(0,que,runro );
		Elevator ele1=new Elevator(1,que,runro );
		Elevator ele2=new Elevator(2,que,runro );
		Thread Ele0=new Thread(ele0);
		Thread Ele1=new Thread(ele1);
		Thread Ele2=new Thread(ele2);
		Patcher dispatcher=new Patcher(ele0,ele1,ele2, que, null,runro);
		Request req=new Request(ele0,ele1,ele2,que,dispatcher,runro);
		Thread Req=new Thread(req);
		Thread Dispatcher = new Thread(dispatcher);
		Req.setPriority(1);
		Dispatcher.setPriority(10);
		Ele0.setPriority(1);
		Ele1.setPriority(1);
		Ele2.setPriority(1);
		Req.start();
		Ele0.start();
		Ele1.start();
		Ele2.start();
		Dispatcher.start();
	}
}