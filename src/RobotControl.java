 class RobotControl
 {
   private Robot r;
   
   public RobotControl(Robot r)
   {
       this.r = r;
   }

   public void control(int barHeights[], int blockHeights[], int required[], boolean ordered)
   {
	   // The first past can be solved easily with out any arrays as the height of bars and blocks are fixed.
	   // Use the method r.up(), r.down(), r.extend(), r.contract(), r.raise(), r.lower(), r.pick(), r.drop()
	   // The code below will cause first arm to be moved up, the second arm to the right and the third to be lowered. 
	  // move the first arm up by one unit
	   // move the second arm to the right by one unit
	   r.lower();
	   // lowering the third arm by one unit
	   // Part B requires you to access the array barHeights passed as argument as the robot arm must move
	   // over the bars
	   // The third part requires you to access the arrays barHeights and blockHeights 
	   // as the heights of bars and blocks are allowed to vary through command line arguments
	   // The fourth part allows the user  to specify the order in which bars must 
	   // be placed in the target column. This will require you to use the use additional column
	   // which can hold temporary values
	   // The last part requires you to write the code to move from source column to target column using
	   // an additional temporary column but without placing a larger block on top of a smaller block 
	   int times = 0;
	   int block_count = 4;
	   int block_h = 2;
	   int up_h = block_h * block_count + 3;
	   int down_h = 1;
	   int block_drop = 0;
	   int temp;
	   int length = 9;
	   for (int i = 0; i < 4; i++ ) {
		   times+=2;
		   if (times > 4) {
			   for (int j = 0; j < (temp = up_h - 4 - down_h); j++)
				   r.up();
			   System.out.println("@@@@@"+temp);
			   Mv_extand(r, length);
			   r.pick();
			   Mv_contract(r, length);
		   }else {
			   System.out.println("@@-@@"+times);
			   for (int j = 0; j < (temp = up_h - times - down_h); j++)
				   r.up();
			   System.out.println("*****"+temp);
			   Mv_extand(r, length);
			   r.pick();
			   Mv_contract(r, length);
			   while(0 < (temp-block_h*block_drop-block_h)) {
				   System.out.println("&&&&&"+temp);
				   r.down();
				   temp--;
			   }
		   }
		   r.drop();
		   down_h+=2;
		   block_drop++;
	   }
   }
   private void Mv_up(Robot r, int sourceHt) {
	   for(int i = 0; i < sourceHt; i++)
		   r.up();
   }
   
   private void Mv_down(Robot r, int destHt) {
	   for(int i = 0; i < destHt; i++)
		   r.down();
   }
   
   private void Mv_extand(Robot r, int length ) {
	   for (int i = 0; i < length; i++)
		   r.extend(); 
   }
   
   private void Mv_contract(Robot r, int length) {
	   for (int i = 0; i < length; i++)
		   r.contract();
   }
}  

