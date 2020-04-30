import java.util.ArrayList;
import java.util.Stack;

public class Fetch extends sim
{
	int size;
	int N;
	Stack stack;
	int dispatch_counter;
	ArrayList<Integer> disptach_list = new ArrayList<Integer>( );
	

	public Fetch(Stack stack, int N) 
	{
		// TODO Auto-generated constructor stub
		this.stack = stack;
		this.N = N;
	}

	// function to move the instruction to dispatch and increment dispatch counter
	public void move_instruction_to_dispatch(int tag) 
	{
		// TODO Auto-generated method stub
		
		this.disptach_list.add(tag);
		
		this.dispatch_counter++;
		
	}
	
	
	public void remove_instruction_from_dispatch() 
	{
		// TODO Auto-generated method stub
		
		//deleting the moved element from dispatch list
		this.disptach_list.remove(0);
		
		this.dispatch_counter--;
		
	}
	
	
	
	// function to get the for loop size for in order to move data to dispatch list
	public int get_the_for_loop_size_to_move_to_dispatch(int stack_size,int N) 
	{
		// TODO Auto-generated method stub
		int available_dispatch_size;
		int size = 0;
		int for_loop_size = 0;
		
		
		if(stack_size >= N)
		{
			size = N;
		}
		else if(stack_size<N)
		{
			size = stack_size;
		}
		
		available_dispatch_size = (2*N) - this.dispatch_counter;
	//	System.out.println("Available dispatch size"+available_dispatch_size);
		
		if(available_dispatch_size == 0 )
		{
			for_loop_size = 0;
		}
		else if(available_dispatch_size >= size)
		{
			for_loop_size = size;
	//		System.out.println("Available for loop size"+for_loop_size);
			
		}
		else if(available_dispatch_size < size)
		{
			for_loop_size = available_dispatch_size;
	//		System.out.println("Available for loop size"+for_loop_size);
		}
		
		
		return for_loop_size;	
	} // end of this function
	
}
