import java.io.InputStream;
import java.util.*;

class RobotControl
 {
   private Robot r;
   
   public RobotControl(Robot r)
   {
       this.r = r;
   }
   
   public void control(int barHeights[], int blockHeights[], int required[], boolean ordered)
   {   int h; // 2 < h < 14
       int w; // 1 < w < 10
       int d; // 0 < d < h
       int block_h;
	   int max_barH = Math.max(MyMath.max(barHeights[0], barHeights[1], barHeights[2]),
			   MyMath.max(barHeights[3], barHeights[4], barHeights[5]));
	   int max_blockH = maxBlockH(blockHeights);
	   int sum_blockH = blockHSum(blockHeights);
	   int up_h = sum_blockH >= (max_blockH + max_barH)? sum_blockH:max_barH + max_blockH;
	   int[] temp = {};
	   int diffHeight = max_blockH + max_barH - sum_blockH;
	   int tempHeight = up_h == sum_blockH? 0:diffHeight;
	   
	   if (ordered) {
		
	   } else {
		
	   }
	   
	   Mv_up(r, up_h - 1);
	   for(int i = blockHeights.length - 1; i >= 0; i--) {
		   Mv_extend(r, 9);
		   Mv_lower(r, tempHeight);
		   r.pick();
		   Mv_raise(r, tempHeight);
		   tempHeight+= blockHeights[i];
		   Mv_contract(r,9);
		   if (up_h == sum_blockH) {
			   Mv_lower(r, up_h - tempHeight);
			   r.drop();
			   Mv_raise(r, up_h - tempHeight);
		   } else {
			   Mv_lower(r, up_h - tempHeight + diffHeight);
			   r.drop();
			   Mv_raise(r, up_h - tempHeight + diffHeight);
		   }
		   
	   }
   }
   
   private int find_index(int[] blockHeights, int value) {
	   int index = 0;
	   for(int i = 0; i < blockHeights.length; i++)
		   if(blockHeights[i] == value)
			   index = i;
	   return index;	   
   }
   
   private int maxBlockH(int[] blockHeights) {
	   int max_blockH = blockHeights[0];
	   for(int i = 1; i < blockHeights.length; i++) {
		   max_blockH = Math.max(max_blockH, blockHeights[i]);
	   }
	   return max_blockH;
   }
   
   private int blockHSum(int[] blockHeights) {
	   int sum = 0;
	   for(int i = 0; i < blockHeights.length; i++)
		   sum += blockHeights[i];
	   return sum;
   }

   private void Mv_up(Robot r, int sourceHt) {
	   for(int i = 0; i < sourceHt; i++)
		   r.up();
   }
   
   private void Mv_down(Robot r, int destHt) {
	   for(int i = 0; i < destHt; i++)
		   r.down();
   }
   
   private void Mv_extend(Robot r, int ex_length) {
	   for (int i = 0; i < ex_length; i++)
		   r.extend(); 
   }
   
   private void Mv_contract(Robot r, int con_length) {
	   for (int i = 0; i < con_length; i++)
		   r.contract();
   }
   
   private void Mv_raise(Robot r, int ra_height) {
	   for(int i = 0; i < ra_height; i++)
		   r.raise();
   }
   
   private void Mv_lower(Robot r, int lw_height) {
	   for(int i = 0; i < lw_height; i++)
		   r.lower();
   }
}  

