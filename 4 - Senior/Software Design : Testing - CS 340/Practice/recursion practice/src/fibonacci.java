//Fibonacci Series using Recursion 
class fibonacci
{
    public static void main (String args[])
    {
        int n = 5;
        System.out.println("Final: " + fact(n));
    }

    public static int fact(int n){
        System.out.println(n);
        if(n <= 1) return n;
        return n * fact(n-1);
    }
}
/* This code is contributed by Rajat Mishra */