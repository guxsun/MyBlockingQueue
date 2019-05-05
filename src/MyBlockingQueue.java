/**
 * @author guxu@zbj.com
 * @version V1.0
 * @title: 线程同步（执行顺序问题） + 线程通信
 * @date 2019/5/5
 */
public class MyBlockingQueue<T> implements Queue<T> {

//    阻塞队列是这样的一种数据结构
//    它是一个队列（类似于一个List），可以存放0到N个元素。
//    我们可以对这个队列执行插入或弹出元素操作，
//    弹出元素操作就是获取队列中的第一个元素，并且将其从队列中移除；
//    而插入操作就是将元素添加到队列的末尾。
//    当队列中没有元素时，对这个队列的弹出操作将会被阻塞，直到有元素被插入时才会被唤醒；
//    当队列已满时，对这个队列的插入操作就会被阻塞，直到有元素被弹出后才会被唤醒。

    /**
     * 存放元素的数组
     */
    private final Object[] items;

    /**
     * 弹出元素的位置
     */
    private int takeIndex;

    /**
     * 插入元素的位置
     */
    private int putIndex;

    /**
     * 队列中的元素总数
     */
    private int count;

    /**
     * 指定队列大小的构造器
     *
     * @param capacity 队列大小
     */
    public MyBlockingQueue(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException();
        // putIndex, takeIndex和count都会被默认初始化为0
        items = new Object[capacity];
    }

    public boolean push(T item) throws InterruptedException {
        while (true) {
            // 直到队列未满时才执行入队操作并跳出循环
            if (count != items.length) {
                // 执行入队操作，将对象e实际放入队列中
                enqueue(item);
                break;
            }

            // 队列已满的情况下休眠200ms
            Thread.sleep(200L);
        }
        return Boolean.TRUE;
    }

    public T pop() throws InterruptedException {
        while (true) {
            // 直到队列非空时才继续执行后续的出队操作并返回弹出的元素
            if (count != 0) {
                // 执行出队操作，将队列中的第一个元素弹出
                return dequeue();
            }

            // 队列为空的情况下休眠200ms
            Thread.sleep(200L);
        }
    }


    /**
     * 入队操作
     *
     * @param item 待插入的元素
     */
    private void enqueue(T item) {
        // 将对象e放入putIndex指向的位置
        items[putIndex] = item;

        // putIndex向后移一位，如果已到末尾则返回队列开头(位置0)
        if (++putIndex == items.length)
            putIndex = 0;

        // 增加元素总数
        count++;
    }

    private T dequeue() {
        // 取出takeIndex指向位置中的元素
        // 并将该位置清空
        T e = (T) items[takeIndex];
        items[takeIndex] = null;

        // takeIndex向后移一位，如果已到末尾则返回队列开头(位置0)
        if (++takeIndex == items.length)
            takeIndex = 0;

        // 减少元素总数
        count--;

        // 返回之前代码中取出的元素e
        return e;
    }

    public static void main(String[] args) {
        final MyBlockingQueue<Integer> queue = new MyBlockingQueue<Integer>(20);

        // 每个线程执行10次
        final int times = 10;

        // 创建2个生产者线程，向队列中并发放入数字0到19，每个线程放入10个数字
        for (int i = 0; i < 2; ++i) {
            final int offset = i * times;
            new Thread(() -> {
                try {
                    for (int j = 0; j < times; ++j) {
                        queue.push(new Integer(offset + j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }


        // 创建2个消费者线程，从队列中弹出20次数字并打印弹出的数字
        for (int i = 0; i < 2; ++i) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < times; ++j) {
                        Integer element = (Integer) queue.pop();
                        System.out.println("===" + element);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        }


    }

}
