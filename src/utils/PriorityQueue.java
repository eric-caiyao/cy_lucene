package utils;

abstract public class PriorityQueue<T>{
    private T[] array;
    private int nextIndex = 0;

    public PriorityQueue(int size){
        array = (T[]) new Object[size];
    }

    public T pop(){
        T top = array[0];
        array[0] = array[nextIndex - 1];
        nextIndex = nextIndex - 1;
        down();
        return top;
    }

    public T top(){
        return array[0];
    }

    public void push(T o){
        array[nextIndex ++] = o;
        up();
    }

    private void down(){
        T tmpItem;
        int currentIndex = 0;
        while(
                lessThan(array[currentIndex * 2 + 1],array[currentIndex])
                || lessThan(array[currentIndex * 2 + 2],array[currentIndex])
        ){
            tmpItem = array[currentIndex];
            if(lessThan(array[currentIndex * 2 + 1],array[currentIndex * 2 + 2])){
                array[currentIndex] = array[currentIndex * 2 + 1];
                array[currentIndex * 2 + 1] = tmpItem;
                currentIndex = currentIndex * 2 + 1;
            }else{
                array[currentIndex] = array[currentIndex * 2 + 2];
                array[currentIndex * 2 + 2] = tmpItem;
                currentIndex = currentIndex * 2 + 2;
            }
        }
    }

    private void up(){
        T tmpItem;
        int currentIndex = nextIndex - 1;
        while(lessThan(array[currentIndex],array[(currentIndex - 1) / 2])){
            tmpItem = array[currentIndex];
            array[currentIndex] = array[(currentIndex - 1) / 2];
            array[(currentIndex - 1) / 2] = tmpItem;
            currentIndex = (currentIndex  - 1) / 2;
        }
    }

    public abstract boolean lessThan(T left, T right);
}
