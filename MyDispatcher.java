/* Implement this class. */

import java.util.List;
import java.util.concurrent.Semaphore;


public class MyDispatcher extends Dispatcher {

    // Class representing the Robin Round scheduling algorithm
    public class RoundRobinClass extends Dispatcher {

        /**
         * Instantiated a dispatcher object.
         *
         * @param algorithm scheduling algorithm to be used
         * @param hosts     lists of hosts the dispatcher assigns tasks to
         */
        public int lastAssignedNodeID; // last assigned node
        public RoundRobinClass(SchedulingAlgorithm algorithm, List<Host> hosts) {
            super(algorithm, hosts);
            this.lastAssignedNodeID = -1; // we are going to increment it
            // before the assignment
        }
        @Override
        public void addTask(Task task) {
            int noNodes = hosts.size();
            lastAssignedNodeID = (lastAssignedNodeID + 1) % noNodes;
            Host target = hosts.get(lastAssignedNodeID);
            target.addTask(task);
        }
    }
    // Instance of Robin Round
    public RoundRobinClass roundRobinClass;

    // Class representing the Shortest Queue scheduling algorithm
    public class ShortestQueueClass extends Dispatcher {

        /**
         * Instantiated a dispatcher object.
         *
         * @param algorithm scheduling algorithm to be used
         * @param hosts     lists of hosts the dispatcher assigns tasks to
         */
        public ShortestQueueClass(SchedulingAlgorithm algorithm, List<Host> hosts) {
            super(algorithm, hosts);
        }

        @Override
        public void addTask(Task task) {
            int node = findNodeWithShortestQ();
            Host newHost = hosts.get(node);
            newHost.addTask(task);
        }

        // Helper method to find the node with the shortest queue
        public int findNodeWithShortestQ() {
            int selectedNode = 0;
            int minQueueSize = Integer.MAX_VALUE;
            for(int i = 0; i < hosts.size(); i++) {
                int queueSize = hosts.get(i).getQueueSize();
                if(queueSize < minQueueSize) {
                    selectedNode = i;
                    minQueueSize = queueSize;

                }

            }
            return  selectedNode;
        }
    }

    // Instance of Shortest Queue
    public ShortestQueueClass shortestQueueClass;

    // Class representing the Size Interval Task Assignment scheduling algorithm
    public class SizeIntervalTaskAssignment extends Dispatcher {

        /**
         * Instantiated a dispatcher object.
         *
         * @param algorithm scheduling algorithm to be used
         * @param hosts     lists of hosts the dispatcher assigns tasks to
         */
        public int numberOfNodes = 3;
        public SizeIntervalTaskAssignment(SchedulingAlgorithm algorithm, List<Host> hosts) {
            super(algorithm, hosts);
        }

        @Override
        public void addTask(Task task) {
            int assignNodeNumber = nodeAssign(task.getType());
            Host assignedHost = hosts.get(assignNodeNumber);
            assignedHost.addTask(task);
        }

        // Helper method to assign the node based on task type
        public int nodeAssign(TaskType type) {
            if(type == TaskType.SHORT) {
                return 0;
            } else if(type == TaskType.MEDIUM) {
                return 1;
            } else if (type == TaskType.LONG) {
                return 2;
            } else {
                throw  new IllegalArgumentException("Invalid task type");
            }

        }
    }

    // Instance of Size Interval Task Assignment
    public SizeIntervalTaskAssignment sizeIntervalTaskAssignment;

    // Class representing the Least Work Left scheduling algorithm
    public class LeastWorkLeft extends Dispatcher {

        /**
         * Instantiated a dispatcher object.
         *
         * @param algorithm scheduling algorithm to be used
         * @param hosts     lists of hosts the dispatcher assigns tasks to
         */
        public LeastWorkLeft(SchedulingAlgorithm algorithm, List<Host> hosts) {
            super(algorithm, hosts);
        }

        @Override
        public void addTask(Task task) {
            Host selected = findWorkLeft();
            selected.addTask(task);

        }

        // Helper method to find the host with the least work left
        public Host findWorkLeft() {
            Host selectedH = null;
            double remainingWorkLeft = Double.MAX_VALUE;
            for(Host i : hosts) {
                MyHost newI = (MyHost) i;
                double reaminingW = newI.getWorkLeft();
                if(reaminingW < remainingWorkLeft) {
                    remainingWorkLeft = reaminingW;
                    selectedH = i;
                }
            }
            return selectedH;
        }
    }

    // Instance of Least Work Left
    public LeastWorkLeft leastWorkLeft;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        // Create instances of scheduling algorithm based on the algorithm that is provided
        if (this.algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
            roundRobinClass = new RoundRobinClass(algorithm, hosts);
        } else if (this.algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
            shortestQueueClass = new ShortestQueueClass(algorithm, hosts);
        } else if (this.algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
            sizeIntervalTaskAssignment = new SizeIntervalTaskAssignment(algorithm, hosts);
        } else if (this.algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
            leastWorkLeft = new LeastWorkLeft(algorithm, hosts);
        }
    }

    @Override
    public void addTask(Task task) {
        synchronized (this) {
            // Dispatch the task based on the selected scheduling algorithm
            if (this.algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
                roundRobinClass.addTask(task);
            } else if (this.algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
                shortestQueueClass.addTask(task);
            } else if (this.algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
                sizeIntervalTaskAssignment.addTask(task);
            } else if (this.algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
                leastWorkLeft.addTask(task);
            }
        }
    }

}
