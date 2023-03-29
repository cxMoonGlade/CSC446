import java.util.concurrent.*;

public class Server implements Runnable{
    private BlockingQueue<Client> queue;
    private long size;

    public Server(BlockingQueue<Client> q, long size){
        this.queue = q;
        this.size = size;
    }
    // hide this part for non-priority run()
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

    // hide this part for priority run.
    // use 12 threads = 3(serves) * 4(threads per server) 
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

/* Single stands for single server.
 * creates a threadPool with fixed 4 core threads.
 */
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

/* Handler stands for client handler
 *
 */
class Handler implements  Runnable{
    private BlockingQueue<Client> queue;
    private long size;
    public Handler(BlockingQueue<Client> queue, long size){
        this.size = size;
        this.queue = queue;
    }

    // **************************** IMPORTANT ****** PAY ATTENTION HERE ************************************
    // Daming, please modify the code to call back some value to the Main object.
    public void run(){
        try {
            Client client;
            if(queue.size() > 0){
                client = queue.poll();

                // set the field of that client.
                // You can also create a queue of served client if you need
                // otherwise, you can delete this part
                long current = System.currentTimeMillis();
                client.setWaiting_time(current - client.getArrival_time());
                client.setServiced(true);
                client.setService_start_time(current);
                client.setTime_in_system(client.getWaiting_time()+client.getService_time());

                Thread.sleep(client.getService_time());
                System.out.println("Client Succeed ---> " + Main.getConcurrent_users()); // You can always change or delete this line

                // here is a call back method example.
                // because we only do the increment, so it does not matter obout the order, so it is thread safe.
                Main.addConcurrentUser();
                Main.addWaiting(client.getWaiting_time());
            }
            else if (Main.getConcurrent_users() < size) {
                Thread.sleep(20);
            }
        }catch (Exception e){e.toString();}
    }
}
