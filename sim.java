
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.lang.Math;

public class sim 
 {
	static int N;
	static int S;
	static String filepath = new String();
	
	//to identify which cycle the program is in 
	static int cycle_no;
	
	// create tag foe the purpose of hash map
	static int tag = 0;
			
	static int [] Register_that_holds_tag_number = new int [128];
	static String[] Register_that_holds_state = new String [128];
	
	static ArrayList<Integer> deleted_list = new ArrayList<Integer>( );


	public static void main(String[] args) throws FileNotFoundException 
	{
		// TODO Auto-generated method stub
	
		//get the input values S, N and the trace file path
		
		S = Integer.parseInt(args[0]);
		
		N = Integer.parseInt(args[1]);
		
		filepath = args[2];
		
		File validation_file = new File(filepath);
		
		Scanner file = new Scanner(validation_file);
		
		
		//creating a class to store all details related to an instruction and mapping it too the key using hash Map
		Instruction objects_for_each_instruction = null;	
		
		//creating a hash map to store all the details related to a PC string from the trace file 
		HashMap<Integer, Instruction> PC_instruction_details = new HashMap<Integer, Instruction> ();
		
		
		//to hold that particular instruction during the file iteration
		String instruction;
		
		// create tag foe the purpose of hash map
		int tag = 0;
		
		// number of instructions in the trace file
		int no_of_instructions = 0;
		
		//stack is used to push all the values from the trace file into the stack queue in order to dispatch it 
		Stack dummy_stack   = new Stack(); // in reverse order
		Stack stack   = new Stack();	// in correct order
		
		//start the file tracing		
		while(file.hasNextLine())
		{	
			
			instruction = file.nextLine();
			dummy_stack.push(instruction);
			no_of_instructions++;
			
		} // end of trace file

		//in order to reverse the instruction list inside the stack
		String dummy_instruction;
		
		while(!dummy_stack.isEmpty())      
		{
			dummy_instruction = (String) dummy_stack.pop();
			stack.push(dummy_instruction);
		}
		
		//creating the register array list in order to hold the destination details
		
	
		Arrays.fill(Register_that_holds_tag_number, -1);

		Arrays.fill(Register_that_holds_state, "R");
		
		//create class Dispatch in order to move the data from stack to dispatch list
		Fetch fetch = new Fetch(stack,N);
		
		Dispatch dispatch = new Dispatch(N,S);
		
		Issue issue = new Issue(N,S);
		
		
		//for(int i = 0;i<9537;i++)
		//{
			
	
		do
		{	
		perform_execute_function(issue,objects_for_each_instruction,PC_instruction_details,Register_that_holds_tag_number,Register_that_holds_state, dispatch, fetch );
		
		perform_issue_function(dispatch,PC_instruction_details,objects_for_each_instruction, issue);
			
		perform_dispatch_operation(dispatch, fetch,PC_instruction_details,Register_that_holds_tag_number,Register_that_holds_state);		

		
		perform_fetch_operation(fetch, objects_for_each_instruction,PC_instruction_details);
		
		
		} while(advance_cycle(fetch.stack,fetch.disptach_list, issue.execute_list,dispatch.issue_list));

		
	
		//output display
		for(int i=0;i<PC_instruction_details.size();i++)
		{
			
			Instruction obj = null;
			obj = PC_instruction_details.get(i);
			int IF_latency=obj.cycle_number.get(1) - obj.cycle_number.get(0);
			int ID_latency = obj.cycle_number.get(2) - obj.cycle_number.get(1);
			int IS_Latency =  obj.cycle_number.get(3) - obj.cycle_number.get(2);
			int EX_Latency =  obj.cycle_number.get(4) - obj.cycle_number.get(3);
			System.out.println(i+" "+"fu{"+obj.operations_type+"} "+"src{"+obj.source_reg1+","+obj.source_reg2+"} "+"dst{"+obj.dest_reg+"} "+"IF{"+obj.cycle_number.get(0)+","+ IF_latency +"} "+"ID{"+obj.cycle_number.get(1)+","+ID_latency+"} "+"IS{"+obj.cycle_number.get(2)+","+IS_Latency+"} "+"EX{"+obj.cycle_number.get(3)+","+EX_Latency+"} "+"WB{"+obj.cycle_number.get(4)+","+"1"+"}");
	

		}
		
		double IPC_Value;
		
		IPC_Value = (double) no_of_instructions / (double) cycle_no;
		
		DecimalFormat dvalue=  new DecimalFormat("##.#####");
		
		
		System.out.println("number of instructions = "+no_of_instructions);
		System.out.println("number of cycles = "+cycle_no);
		System.out.println("IPC = "+dvalue.format(IPC_Value));
		
		
	} // end of main class

	

	private static boolean advance_cycle(Stack stack, ArrayList<Integer> disptach_list, ArrayList<Integer> execute_list, ArrayList<Integer> issue_list) 
	{	
		// TODO Auto-generated method stub
		
		//update cycle no
		cycle_no++;
		
		if(stack.empty() && disptach_list.isEmpty() && execute_list.isEmpty() && issue_list.isEmpty())
		{
		
			return false;
		}
		else
		{	
		return true;
		}
		
	}



	//function that is used to perform the execute stage functions
	private static void perform_execute_function(Issue issue, Instruction objects_for_each_instruction, HashMap<Integer, Instruction> PC_instruction_details, int[] register_that_holds_tag_number, String[] register_that_holds_state, Dispatch dispatch, Fetch fetch) 
	{
		// TODO Auto-generated method stub
		
		ArrayList<Integer> temp_execute_list = new ArrayList<Integer>( );
		
		
		for(int i =0;i<issue.execute_list.size();i++)
		{
			temp_execute_list.add(issue.execute_list.get(i));
		}
		
		
		int for_loop_size = issue.execute_list.size();
		
		for(int i=0; i<for_loop_size;i++)
		{
			int what_value_is_in_the_register;
			int the_tag_number;
			
			
			the_tag_number = issue.execute_list.get(i);
			
			objects_for_each_instruction = 	PC_instruction_details.get(the_tag_number);
			
			
			if(objects_for_each_instruction.cycle_when_I_can_go_in == cycle_no)
			{
				
				temp_execute_list.remove((Object)(the_tag_number));
				
				deleted_list.add(the_tag_number);

				
				//update the instruction state
				objects_for_each_instruction.instruction_state = "WB";
				
				//update cycle number for this instruction(Execute List cycle number (which cycle I came in))
				objects_for_each_instruction.update_cycle_number(cycle_no);
				
				//updating register file arrays
				
				if(objects_for_each_instruction.dest_reg != -1)
				{
				what_value_is_in_the_register = register_that_holds_tag_number[objects_for_each_instruction.dest_reg];
			
				if(the_tag_number == what_value_is_in_the_register )
				{
					register_that_holds_state[objects_for_each_instruction.dest_reg] = "R";
				}	
				}
				
				for(int issue_scan =0;issue_scan<dispatch.issue_list.size();issue_scan++)
				{
					int inorder_to_wake_up;
					
					Instruction ins;
					inorder_to_wake_up =dispatch.issue_list.get(issue_scan);
					ins = 	PC_instruction_details.get(inorder_to_wake_up);
					
					if(ins.S1_dependent_on == the_tag_number)
					{
						ins.S1_state = "R";
						
					}
					 if(ins.S2_dependent_on == the_tag_number)
					{
						ins.S2_state = "R";
						
					}
					 					
				}
				
				for(int issue_scan =0;issue_scan<fetch.disptach_list.size();issue_scan++)
				{
					int inorder_to_wake_up;
					
					Instruction ins;
					inorder_to_wake_up =fetch.disptach_list.get(issue_scan);
					ins = 	PC_instruction_details.get(inorder_to_wake_up);
					if(ins.instruction_state.contentEquals("ID"))
					{
					if(ins.S1_dependent_on == the_tag_number)
					{
						ins.S1_state = "R";
						
					}
					 if(ins.S2_dependent_on == the_tag_number)
					{
						ins.S2_state = "R";
						
					}
					}
					 					
				}
				
			}
		}
		
		issue.execute_list.clear();
		
		for(int i=0;i<temp_execute_list.size();i++)
		{
			issue.execute_list.add(temp_execute_list.get(i));
		}
	}



	//function that is used to perform the issue stage functions
	private static void perform_issue_function(Dispatch dispatch, HashMap<Integer, Instruction> PC_instruction_details, Instruction objects_for_each_instruction, Issue issue) 
	{
		// TODO Auto-generated method stub
		
		int for_loop_value;
		int final_for_loop_value;
		ArrayList<Integer> list_that_is_ready = new ArrayList<Integer>( );
		
		//find out all the instructions whose S1 and S2 are Ready (R)
		for_loop_value = dispatch.function_to_find_which_instructions_have_both_S1_and_S1_ready_state(PC_instruction_details,objects_for_each_instruction);
		
		//writing a function_to_check_if_N+1_value_grater_than_this_for_loop_value
	
		final_for_loop_value = dispatch.function_to_check_if_N_plus_1_value_grater_than_this_for_loop_value(for_loop_value);
		
		
		//find_out_which_instruction_operands_are_ready
		list_that_is_ready  = issue.function_to_find_out_which_instruction_operands_are_ready(PC_instruction_details,objects_for_each_instruction, dispatch);
		
		
		for(int i=0;i<final_for_loop_value;i++)
		{
			
			
			int element_in_the_issue_that_was_moved_to_array_list;
			element_in_the_issue_that_was_moved_to_array_list = list_that_is_ready.get(i);
			
			//remove the instruction from the issue_list // here upcasting is done so check if it will work fine
			
			dispatch.remove_the_instruction_form_the_dispatch_list_and_update_counter(element_in_the_issue_that_was_moved_to_array_list);
		
			
			//adding the removed instruction into the execute_list
			issue.move_instruction_into_the_execute_list(element_in_the_issue_that_was_moved_to_array_list);
			
			
			// getting the object in order to update the state of the instruction
			objects_for_each_instruction = PC_instruction_details.get(element_in_the_issue_that_was_moved_to_array_list);
			
			//update the instruction state
			objects_for_each_instruction.instruction_state = "EX";
			
			//update cycle number for this instruction(Execute List cycle number (which cycle I came in))
			objects_for_each_instruction.update_cycle_number(cycle_no);
			
			//update instruction_execution_latency_for this instruction
			objects_for_each_instruction.update_instruction_execution_latency();
			
			
			
		}
		
	}


	//function that is used to perform the dispatch stage functions
	private static void perform_dispatch_operation(Dispatch dispatch, Fetch fetch, HashMap<Integer, Instruction> PC_instruction_details, int[] Register_that_holds_tag_number, String[] Register_that_holds_state) 
	{
		// TODO Auto-generated method stub
		
		int available_size;
		int element_in_the_dispatch;

		available_size = dispatch.check_available_schedule_size(fetch);
		
			for(int i=0;i<available_size;i++)
			{
				Instruction obj = null;
				
				//get the top element in the dispatch array list
				element_in_the_dispatch = fetch.disptach_list.get(0);
				
				//in order to update the instruction state from ID to IS
				obj = PC_instruction_details.get(element_in_the_dispatch);
				
				if(obj.instruction_state.contentEquals("ID"))
				{
					
					//function to move the instruction to issue list and update schedule counter
					dispatch.move_instruction_to_issue_list(element_in_the_dispatch);
								
					//remove this element form the dispatch list and decrease the value in dispatch counter
					fetch.remove_instruction_from_dispatch();
						
					//update the instruction state
					obj.instruction_state = "IS";
					
					//update cycle number for this instruction(ISSUE LIST cycle number (which cycle I came in))
				    obj.update_cycle_number(cycle_no);
				    	   
				} // end of the main if loop for this instruction
				
			} //end of for loop
			
			//this for loop is used to change the values of instructions in IF to ID state
			
			for (int i=0;i<fetch.disptach_list.size();i++)
			{
				int tag_value_in_dispatch_list = fetch.disptach_list.get(i);
				
				// now access the object values for the tag
				Instruction obj = null;
				obj = PC_instruction_details.get(tag_value_in_dispatch_list);
				
				if(obj.instruction_state.contentEquals("IF"))
				{
					String S1_state = null;
					String S2_state = null;
					
					//update the instruction state
					obj.instruction_state = "ID";
					
					 // to do piece of code with registers (don't forget)
				     
				    //getting the state of S1 and S2 from the registers (if it is ready or not)
				    //updating the state of S1 and S2 for this instruction in Instruction class
				    if(obj.source_reg1 == -1)	
				    {
				    	S1_state = "R";
				    }
				    else if(Register_that_holds_state[obj.source_reg1].contentEquals("R"))
				    {
				    	S1_state = Register_that_holds_state[obj.source_reg1];
				    }
				    else if(Register_that_holds_state[obj.source_reg1].contentEquals("N"))
				    {
				    	S1_state = Register_that_holds_state[obj.source_reg1];
				    	obj.S1_dependent_on = Register_that_holds_tag_number[obj.source_reg1];
				    	
				    }
				    
				    obj.S1_state = S1_state;
				    
				    
				    if(obj.source_reg2 == -1)	
				    {
				    	S2_state = "R";
				    }
				    else if(Register_that_holds_state[obj.source_reg2].contentEquals("R"))
				    {
				    	S2_state = Register_that_holds_state[obj.source_reg2];
				    }
				    else if (Register_that_holds_state[obj.source_reg2].contentEquals("N"))
				    {
				    	S2_state = Register_that_holds_state[obj.source_reg2];
				    	obj.S2_dependent_on = Register_that_holds_tag_number[obj.source_reg2];
				    }
				   
				  
				   obj.S2_state = S2_state;
				 
				   if(obj.dest_reg != -1)
				    {
					   
					   Register_that_holds_tag_number[obj.dest_reg] =   tag_value_in_dispatch_list;
					   Register_that_holds_state[obj.dest_reg] = "N";  
					   		   
				   } // end of the if loop dealing with registers
				   
					
					//update cycle number for this instruction(decode cycle number (which cycle I came in))
					obj.update_cycle_number(cycle_no);
				}
				
			}
			
	
		
		
	}  // end of dispatch function
	
	


	//function that is used to perform the fetch operation
	private static void perform_fetch_operation(Fetch fetch, Instruction objects_for_each_instruction, HashMap<Integer, Instruction> PC_instruction_details) 
	{
		
		// in order to find the stack size in Dispatch class
		int stack_size;
		
		int for_loop_count_based_on_available_counter_size;
		
		// TODO Auto-generated method stub
		if (fetch.stack.size()!=0)
		{	
		//check stack size
		stack_size = fetch.stack.size();
		
		for_loop_count_based_on_available_counter_size = fetch.get_the_for_loop_size_to_move_to_dispatch(stack_size, N);
		
	//	if(for_loop_count_based_on_available_counter_size == 0)
	//	{
	//		break;
	//	}
		
		for(int i =0;i<for_loop_count_based_on_available_counter_size;i++)
		{
			String current_instruction;
			
			current_instruction = (String) fetch.stack.pop();
			
			//creating objects to store each instruction values in hash map 
		    objects_for_each_instruction = new Instruction(current_instruction);
		    
		  //update cycle number for this instruction(fetch cycle number (which cycle I came in))
		    objects_for_each_instruction.update_cycle_number(cycle_no);
		    
		    //pushing the values of the instruction as values into the hash map and assign it to its tag
		    PC_instruction_details.put(tag, objects_for_each_instruction);
		    
		    //update the status for this instruction as IF
		    objects_for_each_instruction.instruction_state = "IF";
		    
		    //function to move the instruction to dispatch queue and update dispatch counter
			fetch.move_instruction_to_dispatch(tag);
			
			tag++;
			
		} // end of for loop
		
		
		} // end of while(stack_size != 0) loop
		
		
	} // end of perform_fetch_operation function 
	
	} // end of the class
		

