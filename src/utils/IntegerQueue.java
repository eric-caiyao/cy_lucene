package utils;

public class IntegerQueue extends PriorityQueue<Integer>{
    public IntegerQueue(int size) {
        super(size);
    }

    @Override
    public boolean lessThan(Integer left, Integer right) {
        return left < right;
    }

    public static void main(String[] args){
        IntegerQueue queue = new IntegerQueue(10);
        queue.push(4);
        queue.push(3);
        queue.push(7);
        queue.push(9);
        queue.push(1);
        queue.push(90);
        queue.push(3);
        queue.push(11);
        queue.push(4);
        queue.push(20);

        for(int i = 0; i < 10; i ++){
            System.out.println(queue.pop());
        }
    }
}
