import java.util.Queue;
import java.util.concurrent.*;

public class Server implements Runnable{
    private BlockingQueue<Client> queue;
    private long size;

    public Server(BlockingQueue<Client> q, long size){
        this.queue = q;
        this.size = size;
    }
    /*
    @Override
    public void run() {
        Thread t1 = new Single(queue, size);
        t1.setPriority(10);
        Thread t2 = new Single(queue, size);
        t2.setPriority(5);
        Thread t3 = new Single(queue, size);
        t3.setPriority(1);
        t1.start();
        t2.start();
        t3.start();
    }
    */
    //*
    @Override
    public void run(){
        ExecutorService es = Executors.newFixedThreadPool(12);
        while (Main.getConcurrent_users()<size){
            Runnable t1 = new Handler(queue, size);
            es.submit(t1);
        }
        es.shutdown();
    }
    //*/
}

class Single extends Thread{
    private BlockingQueue<Client> queue;
    private long size;
    public Single(BlockingQueue<Client> queue, long size){
        this.size = size;
        this.queue = queue;
    }

    public void run(){
        ExecutorService es = Executors.newFixedThreadPool(4);
        while(Main.getConcurrent_users() < size ){
            Runnable t1 = new Handler(queue, size);
            es.submit(t1);
        }
        es.shutdown();
    }
}
class Handler implements  Runnable{
    private BlockingQueue<Client> queue;
    private long size;
    public Handler(BlockingQueue<Client> queue, long size){
        this.size = size;
        this.queue = queue;
    }


    public void run(){
        try {
            Client client;
            if(queue.size() > 0){
                client = queue.poll();
                //client.setService_start_time(System.currentTimeMillis());
                Thread.sleep(client.getService_time());
                System.out.println("Client Succeed");
                Main.addConcurrentUser();
            }
            else if (Main.getConcurrent_users() < size) {
                Thread.sleep(20);
            }
        }catch (Exception e){e.toString();}
    }
}
