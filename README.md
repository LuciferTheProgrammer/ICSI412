# ICSI412 - Operating Systems
Applications are contained in the OS folder.

This repository contains a Java-based operating system simulation project created for an Operating Systems course. The project implements several core OS concepts, including process management, scheduling, kernel calls, inter-process communication, device handling, virtual file systems, memory management, virtual memory, paging, and swapping.

The program simulates a small operating system where userland processes communicate with the OS through system calls. A central kernel handles process scheduling, device access, messaging, and memory operations.

## Features

- Java-based operating system simulation
- Userland process abstraction
- Kernel and OS system call handling
- Process Control Block implementation
- Priority-based scheduler
- Cooperative multitasking
- Timer-based process switching
- Process sleeping and waking
- Process priority demotion
- Inter-process communication using kernel messages
- Virtual file system support
- Random device and fake file system devices
- Open, close, read, write, and seek operations
- Virtual memory allocation and freeing
- Virtual-to-physical page mapping
- Translation Lookaside Buffer simulation
- Paging and swap file support
- Memory isolation tests between processes

## Project Files

- `Main.java` - starts the operating system simulation
- `OS.java` - provides the system call interface between userland processes and the kernel
- `Kernel.java` - handles kernel-level operations such as scheduling, process creation, messaging, devices, and memory management
- `Scheduler.java` - manages process queues, sleeping processes, process switching, and priority demotion
- `PCB.java` - represents a Process Control Block for each process
- `Process.java` - base class for simulated processes using threads and semaphores
- `UserlandProcess.java` - base class for user-level processes
- `KernelMessage.java` - represents messages sent between processes
- `Device.java` - interface for device operations
- `VirtualFileSystem.java` - routes device calls to supported devices
- `FakeFileSystem.java` - simulates file access using random access files
- `RandomDevice.java` - simulates a random byte device
- `Hardware.java` - simulates physical memory and TLB-based address translation
- `VirtualToPhysicalMapping.java` - stores virtual-to-physical and disk page mappings
- `Testing.java` - launches test processes and validates OS functionality
- `Pages1.java` through `Pages5.java` - test memory allocation, read/write behavior, isolation, invalid access, and freeing memory
- `Piggy.java` - stress-tests memory allocation, paging, and swapping
- `Ping.java` and `Pong.java` - test inter-process communication
- `HelloWorld.java`, `GoodbyeWorld.java`, `IdleProcess.java`, and `BlastPast.java` - test scheduling, sleeping, priority behavior, and device operations

## How It Works

The project starts from `Main.java`, which creates an initial userland process and starts the OS. The `OS` class acts as the gateway between userland processes and the kernel. User processes request services such as creating processes, sleeping, reading from devices, sending messages, or allocating memory through OS system calls.

The `Kernel` receives these requests and dispatches them to the proper kernel-level function. It manages process creation, process exit, device access, message passing, memory allocation, and virtual memory translation.

The `Scheduler` decides which process runs next. It uses priority queues for realtime, interactive, and background processes. It also supports sleeping processes and demotes processes that use too many consecutive time slices.

The memory system simulates virtual memory by translating virtual addresses into physical addresses. The `Hardware` class simulates physical memory and a small TLB. If a virtual page is not currently mapped, the kernel assigns a physical page or swaps another page out to disk.

The device system uses a `VirtualFileSystem` to route operations to different devices, including a fake file system and a random device.

## Technologies Used

- Java
- Threads
- Semaphores
- Timers
- Queues
- HashMaps
- File I/O
- RandomAccessFile
- Process scheduling
- Virtual memory simulation
- Paging and swapping
- Inter-process communication

## Purpose

This project was created for an Operating Systems course to practice implementing major OS concepts in Java. It demonstrates how an operating system can manage processes, schedule CPU time, handle system calls, pass messages between processes, manage devices, and simulate virtual memory with paging and swapping.
