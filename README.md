# Dynamic-Instruction-Scheduling
A simulator for an out-of-order superscalar processor based on Tomasulo’s algorithm that fetches, dispatches, and issues N instructions per cycle.

The simulator reads a trace file in the following format:
<PC> <operation type> <dest reg #> <src1 reg #> <src2 reg #>
<PC> <operation type> <dest reg #> <src1 reg #> <src2 reg #>
  
Here,

<PC> is the program counter of the instruction (in hex).
  
<operation type> is either “0”, “1”, or “2”.
  
<dest reg#> is the destination register of the instruction. If it is -1, then the instruction does
not have a destination register (for example, a conditional branch instruction). Otherwise, it
is between 0 and 127.

<src1 reg #> is the first source register of the instruction. If it is -1, then the
instruction does not have a first source register. Otherwise, it is between 0 and 127.

<src2 reg #> is the second source register of the instruction. If it is -1, then the instruction does not have a second source register. Otherwise, it is between 0 and 127.


This simulator accepts command-line arguments as follows:

sim <S> <N> <tracefile>

Here, <S> is the Scheduling Queue size, <N> is the peak fetch and dispatch rate, issue rate will be up to N+1 and <tracefile> is the filename of the input trace.
