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
   {   //int h; // 2 < h < 14
       //int w; // 1 < w < 10
       //int d; // 0 < d < h
       //int block_h;
	   int maxBarH = Math.max(MyMath.max(barHeights[0], barHeights[1], barHeights[2]),
			   MyMath.max(barHeights[3], barHeights[4], barHeights[5]));
	   int maxBlockH = maxBlockH(blockHeights);
	   int sumBlockH = blockHSum(blockHeights);
	   int upH = sumBlockH >= (maxBlockH + maxBarH)? sumBlockH:maxBarH + maxBlockH;
	   int diffHeight = maxBlockH + maxBarH - sumBlockH;
	   int tempHeight = upH == sumBlockH? 0:diffHeight;
	   // program start
	   mvUp(r, upH - 1);
	   if (compareArray(blockHeights, required)) {
		   ArrayList<Integer> temp = new ArrayList<Integer>();
		   ArrayList<Integer> copyBlockHeights = new ArrayList<Integer>();
		   copyBlockHeights = copyArray(copyBlockHeights, blockHeights);
		   int lastBlockHeight = 0;
		   int tempBlocksHeightSum = 0;
		   for (int i = 0; i < required.length; i++) {
			   int blockIndex = copyBlockHeights.indexOf(required[i]);
			   int tempBlockIndex = temp.indexOf(required[i]);
			   if (blockIndex != -1) {
				   mvExtend(r, 8);
				   for (int j = copyBlockHeights.size() - 1; j > blockIndex; j--) {
					   int rmHeight = copyBlockHeights.remove(j);
					   temp.add(rmHeight);
					   mvExtend(r, 1);
					   mvLower(r, tempHeight);
					   r.pick();
					   mvRaise(r, tempHeight);
					   tempHeight+= rmHeight;
					   sumBlockH -= rmHeight;
					   tempBlocksHeightSum += rmHeight;
					   mvContract(r, 1);
					   mvLower(r, upH - tempBlocksHeightSum);
					   r.drop();
					   mvRaise(r, upH - tempBlocksHeightSum);
				   }
				   mvExtend(r, 1);
				   mvLower(r, tempHeight);
				   r.pick();
				   mvRaise(r, tempHeight);
				   tempHeight += copyBlockHeights.get(copyBlockHeights.size()-1);
				   mvContract(r, 9);
				   lastBlockHeight += copyBlockHeights.remove(copyBlockHeights.size()-1);
				   mvLower(r, upH - lastBlockHeight);
				   r.drop();
				   mvRaise(r, upH - lastBlockHeight);
			   } else if (tempBlockIndex != -1) {
				   mvExtend(r, 9);
				   for (int j = temp.size() - 1; j > tempBlockIndex ; j--) {
					   int rmHeight = temp.remove(j);
					   copyBlockHeights.add(rmHeight);
					   sumBlockH += rmHeight;
					   tempHeight-= rmHeight;
					   mvContract(r, 1);
					   mvLower(r, upH - tempBlocksHeightSum);
					   r.pick();
					   mvRaise(r, upH - tempBlocksHeightSum);
					   tempBlocksHeightSum -= rmHeight;
					   mvExtend(r, 1);
					   mvLower(r, upH - rmHeight);
					   r.drop();
					   mvRaise(r, upH - rmHeight);
				   }
				   mvContract(r, 1);
				   mvLower(r, upH - tempBlocksHeightSum);
				   r.pick();
				   mvRaise(r, upH - tempBlocksHeightSum);
				   mvContract(r, 8);
				   lastBlockHeight += temp.remove(temp.size() - 1);
				   mvLower(r, upH - lastBlockHeight);
				   r.drop();
				   mvRaise(r, upH - lastBlockHeight);
			   } else {
				   System.out.println("Error no required element in block array!");
			   }
		   }
	   } else {
		   for(int i = blockHeights.length - 1; i >= 0; i--) {
			   mvExtend(r, 9);
			   mvLower(r, tempHeight);
			   r.pick();
			   mvRaise(r, tempHeight);
			   tempHeight += blockHeights[i];
			   mvContract(r,9);
			   if (upH == sumBlockH) {
				   mvLower(r, upH - tempHeight);
				   r.drop();
				   mvRaise(r, upH - tempHeight);
			   } else {
				   mvLower(r, upH - tempHeight + diffHeight);
				   r.drop();
				   mvRaise(r, upH - tempHeight + diffHeight);
			   }
		   }
	   }  
   }
   private boolean compareArray(int arr1[], int arr2[]) {
	   if (Arrays.equals(arr1, arr2)) {
		   return false; 
	   }
	   int [] temp = new int [arr2.length];
	   for (int i = 0; i < arr2.length; i++) {
		   temp[i] = arr2[arr2.length - i -1];
	   }
	   return !Arrays.equals(arr1, temp);
   }
   
   private ArrayList<Integer> copyArray(ArrayList<Integer>copyArray, int array[]) {
	   for (int i = 0; i < array.length; i++) {
		   copyArray.add(array[i]);
	   }
	   return copyArray;
   }
   
//   private int find_index(int[] blockHeights, int value) {
//	   int index = 0;
//	   for(int i = 0; i < blockHeights.length; i++)
//		   if(blockHeights[i] == value)
//			   index = i;
//	   return index;	   
//   }
   
   private int maxBlockH(int[] blockHeights) {
	   int maxBlockH = blockHeights[0];
	   for(int i = 1; i < blockHeights.length; i++) {
		   maxBlockH = Math.max(maxBlockH, blockHeights[i]);
	   }
	   return maxBlockH;
   }
   
   private int blockHSum(int[] blockHeights) {
	   int sum = 0;
	   for(int i = 0; i < blockHeights.length; i++)
		   sum += blockHeights[i];
	   return sum;
   }

   private void mvUp(Robot r, int sourceHt) {
	   for(int i = 0; i < sourceHt; i++)
		   r.up();
   }
   
   private void mvDown(Robot r, int destHt) {
	   for(int i = 0; i < destHt; i++)
		   r.down();
   }
   
   private void mvExtend(Robot r, int exLength) {
	   for (int i = 0; i < exLength; i++)
		   r.extend(); 
   }
   
   private void mvContract(Robot r, int conLength) {
	   for (int i = 0; i < conLength; i++)
		   r.contract();
   }
   
   private void mvRaise(Robot r, int raHeight) {
	   for(int i = 0; i < raHeight; i++)
		   r.raise();
   }
   
   private void mvLower(Robot r, int lwHeight) {
	   for(int i = 0; i < lwHeight; i++)
		   r.lower();
   }
}  

