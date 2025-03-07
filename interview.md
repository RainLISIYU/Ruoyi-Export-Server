### 集合原理
1. ArrayList：底层有Object数组组成，扩容机制为需要长度大于当前数组长度进行扩容，由0扩容到1或1.5倍旧长度。
2. HashMap：底层由数组和链表组成，数组位置获取算法为(n-1) & (hash ^ hash >>> 16)，扩容系数默认0.75，当存储的数据量大于n*0.75进行扩容，扩容长度为原来1倍，保证n-1都是1，hash算法分布更分散，一个链表中的元素超过8个触发treeifyBin，若数组长度未超过64进行扩容，否则转为红黑树。
3. ConcurrentHashMap：[参考源码解析](https://javaguide.cn/java/collection/concurrent-hash-map-source-code.html#_2-concurrenthashmap-1-8)
4. ArrayBlockingQueue：含有阻塞入队和出队功能，put和take，共用同一个ReentrantLock，通过两个Condition(NotFull和NotEmpty)，入队时判断队列是否已满，若满则NotFull.await，否则元素入队并NotEmpty.signal。出队相反。
<img src="./ArrayBlockingQueue-notEmpty-notFull.png">
### JUC
1. 结合ReentrantLock源码分析AbstractQueuedSynchronizer(AQS)原理。
* 内部抽象类Sync继承AQS。
  * tryLock方法：获取当前线程，获取AQS的state，若state为0，则获取锁，否则判断当前线程是否已获取锁，已获取则state+1，条件都不满足则返回false。
  * tryRelease方法：c为state-releases，判断当前线程是否获取锁，否则异常，判断c==0，是释放锁，然后更新state；
  * lock方法：initialTryLock为false -> acquire(1)
  * AQS的acquire方法：[参考解析](https://blog.csdn.net/TonyAlexer/article/details/144267907)
  * NonfairSync非公平锁子类。
    * initialTryLock方法：获取当前线程，CAS赋值state，成功获取锁，否则判断当前线程是否已获取锁，已获取则state+1，条件都不满足则返回false。
    * tryAcquire方法：获取state==0且CAS赋值，成功获取锁。
* FairSync公平锁子类。
  * 方法同上，只是第一次获取锁时增加判断，只有列表的第一个线程可获取锁。
