public class Handle implements Runnable {
    @Override
    public void run() {
        double t = Math.random()*Double.MAX_VALUE;
        for (double i = 0; i < t; i++);
    }
}
