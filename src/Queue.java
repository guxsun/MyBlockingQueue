/**
 * @author guxu@zbj.com
 * @version V1.0
 * @title: 队列操作相关接口
 * @date 2019/5/5
 */
public interface Queue<T> {

    /**
     * 向队列中添加元素
     *
     * @param item
     * @return
     */
    boolean push(T item) throws InterruptedException;


    /**
     * 从队列中弹出元素
     *
     * @param
     * @return
     */
    T pop() throws InterruptedException;
}
