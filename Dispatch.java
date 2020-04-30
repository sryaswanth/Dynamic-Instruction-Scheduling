import java.util.ArrayList;
import java.util.HashMap;

public class Dispatch extends sim
{
	int  N;
	int S;
	int schedule_counter;
	int available_size;
	ArrayList<Integer> issue_list;
	
	public Dispatch(int N, int S) 
	{
		// TODO Auto-generated constructor stub
		
		this.N = N;
		this.S = S;
		available_size = this.S;
		
		issue_list = new ArrayList<Integer>( );
		
	}

	public int check_available_schedule_size(Fetch fetch) 
	{
//		int size = 0;
		// TODO Auto-generated method stub
		
//		if(available_size - schedule_counter > 0)
//		{
//			size = available_size;
//		}
//		return size;
	
		int for_loop_size = 0;
		
		int fetch_size = fetch.disptach_list.size();
		int free_space_in_schedular_counter = S - this.schedule_counter;
		
		if(fetch_size >=  free_space_in_schedular_counter)
		{
			for_loop_size = free_space_in_schedular_counter;
		}
		else if(fetch_size < free_space_in_schedular_counter)
		{
			for_loop_size = fetch_size;
		}
		
		return for_loop_size;	
		
	}

	public void move_instruction_to_issue_list(int element_in_the_dispatch) 
	{
		// TODO Auto-generated method stub
		
		this.issue_list.add(element_in_the_dispatch);
		
		this.schedule_counter++;
		
	}

	//function_to_find_which_instructions_have_both_S1_and_S1_ready_state
	
	public int function_to_find_which_instructions_have_both_S1_and_S1_ready_state(HashMap<Integer, Instruction> PC_instruction_details, Instruction objects_for_each_instruction) 
	{
		// TODO Auto-generated method stub
		int count_of_positive_values = 0;
		int issue_list_value;
		//check the tags in issue list has both S1 and S2 ready by iterating through it
		for(int i =0;i<issue_list.size();i++)
		{
			issue_list_value = issue_list.get(i);
			objects_for_each_instruction = PC_instruction_details.get(issue_list_value);
			
			if(objects_for_each_instruction.S1_state.contentEquals("R") && objects_for_each_instruction.S2_state.contentEquals("R"))
			{
				count_of_positive_values++;
			}
			
		}
		return count_of_positive_values;
	}
	
	

	public int function_to_check_if_N_plus_1_value_grater_than_this_for_loop_value(int for_loop_value) 
	{
		// TODO Auto-generated method stub
		int final_for_loop_value = 0;
		
		
		if(for_loop_value <= this.N+1 )
		{
			final_for_loop_value = for_loop_value;
		}
		else if(for_loop_value > this.N+1)
		{
			final_for_loop_value = this.N+1;
		}
		
		return final_for_loop_value;
		
	}
	
	

	public void remove_the_instruction_form_the_dispatch_list_and_update_counter(int element_in_the_issue_that_was_moved_to_array_list) 
	{
		// TODO Auto-generated method stub
		
		this.issue_list.remove((Object)(element_in_the_issue_that_was_moved_to_array_list));
		
		this.schedule_counter--;

	}

	
		

	
}
