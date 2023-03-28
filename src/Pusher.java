import java.util.concurrent.BlockingQueue;

public class Pusher implements Runnable{
    private BlockingQueue<Client> queue;
    private long size;
    public Pusher(BlockingQueue<Client> q, long size){
        this.queue = q;
        this.size = size;
    }

    @Override
    public void run(){
        for (int i = 0; i < size;i++){
            long timer = System.currentTimeMillis();
            Client client = new Client(Main.service_time_array[i], Main.inter_arriving_time_array[i]
                    , timer);
            try{Thread.sleep(client.getInter_arrival_time());}
            catch (InterruptedException e){e.printStackTrace();}
            queue.add(client);
            System.out.println("add a client");
        }
    }

}
