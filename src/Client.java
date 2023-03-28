public class Client {
    private boolean serviced;
    private long service_time;
    private long service_start_time;
    private long inter_arrival_time;
    private long arrival_time;

    private double time_in_system;

    public Client(long service_time, long inter_arrival_time, long arrival_time){
        this.serviced = false;
        this.service_time = service_time;
        this.inter_arrival_time = inter_arrival_time;
        this.arrival_time = arrival_time;
    }


    public boolean isServiced() {
        return serviced;
    }

    public void setServiced(boolean serviced) {
        this.serviced = serviced;
    }

    public long getService_time() {
        return service_time;
    }

    public void setService_time(int service_time) {
        this.service_time = service_time;
    }


    public long getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(long arrival_time) {
        this.arrival_time = arrival_time;
    }

    public long getInter_arrival_time() {
        return inter_arrival_time;
    }

    public void setInter_arrival_time(long inter_arrival_time) {
        this.inter_arrival_time = inter_arrival_time;
    }

    public double getTime_in_system() {
        return time_in_system;
    }

    public void setTime_in_system(double time_in_system) {
        this.time_in_system = time_in_system;
    }
    public String toString(){
        return "This Client IAT = " + inter_arrival_time + "; ST = " + service_time + "; AT = " + arrival_time;
    }

    public long getService_start_time(long l) {
        return service_start_time;
    }

    public void setService_start_time(long service_start_time) {
        this.service_start_time = service_start_time;
    }
}
