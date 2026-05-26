package serverTools;

import commands.CommandRequest;
import sharedTools.Deserializer;
import sharedTools.Message;

import java.nio.ByteBuffer;
import java.util.concurrent.*;

public class ClientHandler {
    private ExecutorService readingPool;
    private ForkJoinPool workingPool;
    private ExecutorService sendingPool;
    private LinkedBlockingQueue<Request> requestBuffer;
    private LinkedBlockingQueue<ResultOfRequest> resultBuffer;

    public ClientHandler(){

    }

    public void readingByteRequest(ByteRequest br){
            try{
                requestBuffer.offer(new Request(br.client(),(CommandRequest) Deserializer.deserializeFromBytes( br.bytes().array()) ), 500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    }

    public void workLoop(){
        while(!Thread.interrupted()){
            try{
                Request r = requestBuffer.poll();
                if(r != null){
                    // исполнение
                    Message msg = new Message();
                    resultBuffer.offer(new ResultOfRequest(r.client(), msg) ,500, TimeUnit.MILLISECONDS);
                }
            }catch(InterruptedException e ){
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void sendingLoop(){
        while(!Thread.interrupted()){
            try{
                Request r = requestBuffer.take();
                // исполнение
                Message msg = new Message();
                resultBuffer.offer(new ResultOfRequest(r.client(), msg) ,500, TimeUnit.MILLISECONDS);
            }catch(InterruptedException e ){
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


}
