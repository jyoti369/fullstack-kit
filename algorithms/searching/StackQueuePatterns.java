package algorithms.searching;

import java.util.*;

/**
 * Stack & Queue Patterns
 */
public class StackQueuePatterns {

    // 1. Valid Parentheses
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') stack.push(c);
            else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c==')' && top!='(') return false;
                if (c==']' && top!='[') return false;
                if (c=='}' && top!='{') return false;
            }
        }
        return stack.isEmpty();
    }

    // 2. Daily Temperatures (Monotonic Stack)
    public static int[] dailyTemperatures(int[] temps) {
        int n = temps.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temps[i] > temps[stack.peek()])
                result[stack.peek()] = i - stack.pop();
            stack.push(i);
        }
        return result;
    }

    // 3. Largest Rectangle in Histogram
    public static int largestRectangle(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int max = 0;
        int[] h = Arrays.copyOf(heights, heights.length + 1); // sentinel
        for (int i = 0; i <= h.length - 1; i++) {
            while (!stack.isEmpty() && h[i] < h[stack.peek()]) {
                int height = h[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, height * width);
            }
            stack.push(i);
        }
        return max;
    }

    // 4. Min Stack
    static class MinStack {
        private Deque<Integer> stack = new ArrayDeque<>();
        private Deque<Integer> minStack = new ArrayDeque<>();
        public void push(int val) {
            stack.push(val);
            minStack.push(minStack.isEmpty() ? val : Math.min(val, minStack.peek()));
        }
        public void pop() { stack.pop(); minStack.pop(); }
        public int top() { return stack.peek(); }
        public int getMin() { return minStack.peek(); }
    }

    // 5. Evaluate RPN
    public static int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String t : tokens) {
            switch (t) {
                case "+" -> stack.push(stack.pop() + stack.pop());
                case "*" -> stack.push(stack.pop() * stack.pop());
                case "-" -> { int b = stack.pop(); stack.push(stack.pop() - b); }
                case "/" -> { int b = stack.pop(); stack.push(stack.pop() / b); }
                default  -> stack.push(Integer.parseInt(t));
            }
        }
        return stack.peek();
    }

    public static void main(String[] args) {
        System.out.println(isValid("({[]})"));              // true
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{73,74,75,71,69,72,76,73})));
        System.out.println(largestRectangle(new int[]{2,1,5,6,2,3})); // 10
        System.out.println(evalRPN(new String[]{"2","1","+","3","*"})); // 9
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Stack & Queue Patterns
 */
public class StackQueuePatterns {

    // 1. Valid Parentheses
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') stack.push(c);
            else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c==')' && top!='(') return false;
                if (c==']' && top!='[') return false;
                if (c=='}' && top!='{') return false;
            }
        }
        return stack.isEmpty();
    }

    // 2. Daily Temperatures (Monotonic Stack)
    public static int[] dailyTemperatures(int[] temps) {
        int n = temps.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temps[i] > temps[stack.peek()])
                result[stack.peek()] = i - stack.pop();
            stack.push(i);
        }
        return result;
    }

    // 3. Largest Rectangle in Histogram
    public static int largestRectangle(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int max = 0;
        int[] h = Arrays.copyOf(heights, heights.length + 1); // sentinel
        for (int i = 0; i <= h.length - 1; i++) {
            while (!stack.isEmpty() && h[i] < h[stack.peek()]) {
                int height = h[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, height * width);
            }
            stack.push(i);
        }
        return max;
    }

    // 4. Min Stack
    static class MinStack {
        private Deque<Integer> stack = new ArrayDeque<>();
        private Deque<Integer> minStack = new ArrayDeque<>();
        public void push(int val) {
            stack.push(val);
            minStack.push(minStack.isEmpty() ? val : Math.min(val, minStack.peek()));
        }
        public void pop() { stack.pop(); minStack.pop(); }
        public int top() { return stack.peek(); }
        public int getMin() { return minStack.peek(); }
    }

    // 5. Evaluate RPN
    public static int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String t : tokens) {
            switch (t) {
                case "+" -> stack.push(stack.pop() + stack.pop());
                case "*" -> stack.push(stack.pop() * stack.pop());
                case "-" -> { int b = stack.pop(); stack.push(stack.pop() - b); }
                case "/" -> { int b = stack.pop(); stack.push(stack.pop() / b); }
                default  -> stack.push(Integer.parseInt(t));
            }
        }
        return stack.peek();
    }

    public static void main(String[] args) {
        System.out.println(isValid("({[]})"));              // true
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{73,74,75,71,69,72,76,73})));
        System.out.println(largestRectangle(new int[]{2,1,5,6,2,3})); // 10
        System.out.println(evalRPN(new String[]{"2","1","+","3","*"})); // 9
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Stack & Queue Patterns
 */
public class StackQueuePatterns {

    // 1. Valid Parentheses
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') stack.push(c);
            else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if (c==')' && top!='(') return false;
                if (c==']' && top!='[') return false;
                if (c=='}' && top!='{') return false;
            }
        }
        return stack.isEmpty();
    }

    // 2. Daily Temperatures (Monotonic Stack)
    public static int[] dailyTemperatures(int[] temps) {
        int n = temps.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temps[i] > temps[stack.peek()])
                result[stack.peek()] = i - stack.pop();
            stack.push(i);
        }
        return result;
    }

    // 3. Largest Rectangle in Histogram
    public static int largestRectangle(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int max = 0;
        int[] h = Arrays.copyOf(heights, heights.length + 1); // sentinel
        for (int i = 0; i <= h.length - 1; i++) {
            while (!stack.isEmpty() && h[i] < h[stack.peek()]) {
                int height = h[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, height * width);
            }
            stack.push(i);
        }
        return max;
    }

    // 4. Min Stack
    static class MinStack {
        private Deque<Integer> stack = new ArrayDeque<>();
        private Deque<Integer> minStack = new ArrayDeque<>();
        public void push(int val) {
            stack.push(val);
            minStack.push(minStack.isEmpty() ? val : Math.min(val, minStack.peek()));
        }
        public void pop() { stack.pop(); minStack.pop(); }
        public int top() { return stack.peek(); }
        public int getMin() { return minStack.peek(); }
    }

    // 5. Evaluate RPN
    public static int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String t : tokens) {
            switch (t) {
                case "+" -> stack.push(stack.pop() + stack.pop());
                case "*" -> stack.push(stack.pop() * stack.pop());
                case "-" -> { int b = stack.pop(); stack.push(stack.pop() - b); }
                case "/" -> { int b = stack.pop(); stack.push(stack.pop() / b); }
                default  -> stack.push(Integer.parseInt(t));
            }
        }
        return stack.peek();
    }

    public static void main(String[] args) {
        System.out.println(isValid("({[]})"));              // true
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{73,74,75,71,69,72,76,73})));
        System.out.println(largestRectangle(new int[]{2,1,5,6,2,3})); // 10
        System.out.println(evalRPN(new String[]{"2","1","+","3","*"})); // 9
    }
}
