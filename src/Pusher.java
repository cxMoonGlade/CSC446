import java.util.concurrent.BlockingQueue;

/* Pusher stands for client pusher, it adds client to Main.clients
 *
 */
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
            queue.add(client);
            // System.out.println("add a client->" + i);// you can always delete or change this line

            // after the clients clearly identified and added to the queue,
            // let the thread sleep to simulate the processing.
            try{Thread.sleep(client.getInter_arrival_time());}
            catch (InterruptedException e){e.printStackTrace();}
        }
    }

}
