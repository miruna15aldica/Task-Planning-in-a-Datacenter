# TEMA 1 APD
# Aldica Maria-Miruna, 331CA

## MyDispatcher

- The MyDispatcher class is designed to manage task dispatching for various
scheduling algorithms, including Robin Round, Shortest Queue, Size Interval
Task Assignment and Least Work Left.

I did for each algorithm an inner class:

*Robin Round*
- The algorithm assigns tasks to hosts in a sequential order.
The 'lastAssignedNodeID' variable keeps track of the last assigned host,
each task is assigned to the next host.

*Shortest Queue*
- The algorithm selects the host with the shortest queue
to assign a new task, using the method 'findNodesWithShortestQ'.

*Size Interval Task Assignment*
- The algorithm categorizes hosts into
different nodes based on the task type (SHORT, MEDIUM, LONG) and
the tasks are distributed according to their designated type.

*Least Work Left*
- The algorithm selects the host with the least
cumulative work left to receive a new task, using the 'findWorkLeft'
method.

*MyDispatcher*
- The dispatcher uses an if-else statement in the constructor to instantiate
the right scheduling algorithm based on the enumeration (each algorithm is
implemented in a different class to make sure we encapsulate the logic, as
I previously said).

*addTask*
- We apply synchronization when we are adding the tasks to ensure the
thread's safety.

## MyHost

*PriorityBlockingQueue<Task> pq*
- is utilized to managed task based on
the reversed priority order and ID (I used a comparator for storing the tasks
in the right order)

*AtomicBoolean stop*
- it is used to control the host's threads. They continue
running until the 'stop' signal is received.

*run*
- represents the main execution logic of the host.
- it runs while the stop is false. The current task is in the beginning null.
- if our queue is not empty, the algorithm determines the current task.
- the choice of the current task depends on whether the 'lastTask' exist and it is not preemptive.
If so, the lastTask continues its execution. Otherwise, the current task is set to the
highest priority using 'pq.peek()'.
- we use startTime to update the task's remaining time and if the remaining time becomes
less than 0,  the task is finished, removed from pq and our variables for current and last
task are being reset.

*addTask*
- responsible for adding a new task to the host's priority blocking queue

*getQueueSize*
- responsible for getting the priority blocking queue size

*getWorkLeft*
- calculates the total remaining work across all tasks in the host's priority
blocking queue

*shutdown*
- initiating shutdown