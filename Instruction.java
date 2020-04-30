import java.util.ArrayList;
import java.util.HashMap;

public class Instruction extends sim
{
	 String message;
	int operations_type;
	int dest_reg;
	int source_reg1;
	int source_reg2;
	String[] contents;
	String instruction_state;
	int instruction_execution_latency;
	int S1_dependent_on;
	int S2_dependent_on;
	int cycle_when_I_can_go_in;
	
	
	
	String S1_state;
	String S2_state;
	
	ArrayList<Integer> cycle_number = new ArrayList<Integer>( );
	ArrayList<Integer> cycle_stay = new ArrayList<Integer>( );
	
	
	
	public Instruction(String instruction) 
	{
		// TODO Auto-generated constructor stub2
		contents = instruction.split(" ");
		
		this.message = contents[0];
	    this.operations_type = Integer.parseInt(contents[1]);
	    this.dest_reg = Integer.parseInt(contents[2]);
	    this.source_reg1 = Integer.parseInt(contents[3]);
	    this.source_reg2 = Integer.parseInt(contents[4]);
	    
	    //doing this since S1 and S2 shoud not confuse that it is dependent on 0 during initial declaration
	    this.S1_dependent_on = -1;
	    this.S2_dependent_on = -1;
	    
	}
	
	
	public void update_cycle_number(int cycle_no)
	{
		this.cycle_number.add(cycle_no);		
	}

	
	public void update_instruction_execution_latency() 
	{
		// TODO Auto-generated method stub
		if(this.operations_type == 0)
		{
			this.instruction_execution_latency = 1;
			this.cycle_when_I_can_go_in = this.cycle_number.get(3) + instruction_execution_latency ;
		}
		else if(this.operations_type == 1)
		{
			this.instruction_execution_latency = 2;
			this.cycle_when_I_can_go_in = this.cycle_number.get(3) + instruction_execution_latency ;
		}
		else if(this.operations_type == 2)
		{
			this.instruction_execution_latency = 5;
			this.cycle_when_I_can_go_in = this.cycle_number.get(3) + instruction_execution_latency ;
		}
		
	}
	
	
}
