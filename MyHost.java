/* Implement this class. */

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyHost extends Host {

    // Priority Blocking queue for storing tasks based on priority and id
    public final PriorityBlockingQueue<Task> pq = new PriorityBlockingQueue<>(20, Comparator
            .<Task, Integer>comparing(Task::getPriority, Comparator.reverseOrder())
            .thenComparing(Task::getId));

    // Atomic boolean to control the host thread
    public final AtomicBoolean stop = new AtomicBoolean(false);

    // Current task
    Task currentTask = null;

    // Last task
    Task lastTask = null;

    // The start time
    private long startTime;
    @Override
    public void run() {
        // Continue running until the stop signal is received
        while (!stop.get()) {
                // The task is currently null
                currentTask = null;
                // Checking if there are tasks in the queue
                if (pq.size() > 0) {
                    // Determine the current task based on thr preemptiveness of the last one
                    if(lastTask != null && !lastTask.isPreemptible()) {
                        currentTask = lastTask;
                    } else {
                        currentTask = pq.peek();
                    }
                    // Setting the start time
                    startTime = System.currentTimeMillis();
                }
                // if the current task exists
                if (currentTask != null) {
                    lastTask = currentTask;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Based on the elapsed time, update the task's time
                    long duration = System.currentTimeMillis() - startTime;
                    currentTask.setLeft((currentTask.getLeft() - duration));
                    // Finish the task if the remaining time is less than 0
                    if (currentTask.getLeft() < 0) {
                        currentTask.finish();
                        // remove the task from queue
                        pq.remove(currentTask);
                        currentTask = null;
                        lastTask = null;

                    }
                }
        }
    }

    @Override
    public void addTask(Task task1) {
        pq.offer(task1);
    }

    @Override
    public int getQueueSize() {
        synchronized (pq) {
            if (currentTask == null) {
                return pq.size();
            }
        }
        // If the current task exists, we will include it as well
        return pq.size() + 1;
    }

    @Override
    public long getWorkLeft() {
        int work = 0;
        synchronized (pq) {
            for (Task i : pq) {
                work += i.getLeft();
            }
        }

        return work;
    }

    @Override
    public void shutdown() {
        stop.set(true);
    }

}
