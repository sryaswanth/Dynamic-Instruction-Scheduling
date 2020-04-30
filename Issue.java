import java.util.ArrayList;
import java.util.HashMap;

public class Issue extends sim
{
	int N;
	int S;
	ArrayList<Integer> execute_list;
	
	public Issue(int N, int S) 
	{
		// TODO Auto-generated constructor stub
		this.N = N;
		this.S = S;
		execute_list = new ArrayList<Integer>( );
	}

	
	//function_to_find_out_which_instruction_operands_are_ready
	
	public ArrayList<Integer> function_to_find_out_which_instruction_operands_are_ready(HashMap<Integer, Instruction> PC_instruction_details, Instruction objects_for_each_instruction, Dispatch dispatch) 
	{
		// TODO Auto-generated method stub
		ArrayList<Integer> list_that_has_operands_ready = new ArrayList<Integer>( );
		int issue_list_value;
		//check the tags in issue list has both S1 and S2 ready by iterating through it
		for(int i =0;i<dispatch.issue_list.size();i++)
		{
			issue_list_value = dispatch.issue_list.get(i);
			objects_for_each_instruction = PC_instruction_details.get(issue_list_value);
			
			if(objects_for_each_instruction.S1_state.contentEquals("R") && objects_for_each_instruction.S2_state.contentEquals("R"))
			{
				list_that_has_operands_ready.add(issue_list_value);
			}
			
		}
		return list_that_has_operands_ready;
		
	}


	public void move_instruction_into_the_execute_list(int element_in_the_issue_that_was_moved_to_array_list) 
	{
		// TODO Auto-generated method stub
		
		execute_list.add(element_in_the_issue_that_was_moved_to_array_list);	
	}

}
