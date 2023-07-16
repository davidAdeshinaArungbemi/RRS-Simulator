Round robin scheduling is a CPU scheduling algorithm that assigns each process a fixed time slot, called a time quantum. The process that is currently running is preempted (forced to give up the CPU) if its time quantum expires. The preempted process is then placed at the end of the ready queue, which is a list of processes that are waiting to be executed. The next process in the ready queue is then given the CPU. This process continues until all of the processes in the ready queue have been executed. Round robin scheduling is a preemptive algorithm that ensures that all of the processes in the ready queue get a fair share of the CPU. It is also a time-sharing algorithm that is designed to allow multiple processes to run concurrently.

This Project, written in Java was built to simulate how the round robin algorithm decides how and when tasks are assigned to the CPU.
GUI was created with JAVA swing.

<img width="389" alt="image" src="https://github.com/eminenceinthelight/RRS-Simulator/assets/105245707/f5cf0d37-5499-40ed-b317-1afac2f03b4e">

