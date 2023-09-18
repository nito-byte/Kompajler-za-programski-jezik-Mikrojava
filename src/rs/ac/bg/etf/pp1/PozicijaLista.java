package rs.ac.bg.etf.pp1;
import static java.lang.System.exit;
 
// Create Stack Using Linked list
class PozicijaLista {
 
    // A linked list node
    private class Node {
 
        int data; // integer data
        Node link; // reference variable Node type
    }
    
    Node top;       // top reference variable global
    PozicijaLista() { this.top = null; }
 
    // Utility function to add an element x in the stack
    public void ubaci(int x) // insert at the beginning
    {
        // create new node temp and allocate memory
        Node temp = new Node();
 
        temp.data = x;
        temp.link = top;
        top = temp;
    }
 
    // Utility function to check if the stack is empty or not
    public boolean isEmpty()
    {
        return top == null;
    }
 
    // Utility function to return top element in a stack
    public int topElement()
    {
        // check for empty stack
        if (!isEmpty()) {
            return top.data;
        }
        else {
            System.out.println("Stack is empty");
            return -1;
        }
    }
 
    // Utility function to pop top element from the stack
    public int skini() // remove at the beginning
    {
        // check for stack underflow
        if (top == null) {
            System.out.print("\nStack Underflow");
            return 0;
        }
 
        // update the top pointer to point to the next node
        int vr = top.data;
        top = (top).link;
        return vr;
    }
 
    public void display()
    {
        // check for stack underflow
        if (top == null) {
            System.out.printf("\nStack Underflow");
            exit(1);
        }
        else {
            Node temp = top;
            while (temp != null) {
 
                // print node data
                System.out.printf("%d->", temp.data);
 
                // assign temp link to temp
                temp = temp.link;
            }
        }
    }
}
