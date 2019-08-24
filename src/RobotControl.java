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
	   
	   int maxBarH = maxBlockH(barHeights);
	   int maxBlockH = maxBlockH(blockHeights);
	   int sumBlockH = blockHSum(blockHeights);
	   // r.up() height: if the sum of blocks height is higher than the sum of max Bar' height plus max Block' height, choose the higher one
	   int upH = sumBlockH >= (maxBlockH + maxBarH)? sumBlockH:maxBarH + maxBlockH;
	   int diffHeight = maxBlockH + maxBarH - sumBlockH;
	   int tempHeight = upH == sumBlockH? 0:diffHeight;
	   // program start
	   mvUp(r, upH - 1);
	   if (compareArray(blockHeights, required)) {
		   // part D
		   // create a temporary ArrayList(or Array) for moving and storing blocks	   
		   ArrayList<Integer> temp = new ArrayList<Integer>();
		   // create a ArrayList copy of Array blockHeights for operating elements in this array		   
		   ArrayList<Integer> copyBlockHeights = new ArrayList<Integer>();
		   copyBlockHeights = copyArray(copyBlockHeights, blockHeights);
		   //summing the height of dropped blocks at destination 
		   int lastBlockHeight = 0;
		   //summing the height of dropped blocks at temporary area
		   int tempBlocksHeightSum = 0;
		   for (int i = 0; i < required.length; i++) {
			   // find required block from temporary and departure area
//			   int blockIndex = findIndex(copyBlockHeights,required[i]);
//			   int tempBlockIndex = findIndex(temp,required[i]);
			   int blockIndex = copyBlockHeights.lastIndexOf(required[i]);
			   int tempBlockIndex = temp.lastIndexOf(required[i]);
			   // block is found from departure area
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
				   mvExtend(r, 8);
				   for (int j = temp.size() - 1; j > tempBlockIndex ; j--) {
					   int rmHeight = temp.remove(j);
					   copyBlockHeights.add(rmHeight);
					   sumBlockH += rmHeight;
					   tempHeight-= rmHeight;
					   mvLower(r, upH - tempBlocksHeightSum);
					   r.pick();
					   mvRaise(r, upH - tempBlocksHeightSum);
					   tempBlocksHeightSum -= rmHeight;
					   mvExtend(r, 1);
					   mvLower(r, tempHeight);
					   r.drop();
					   mvRaise(r, tempHeight);
					   mvContract(r, 1);
				   }
				   mvLower(r, upH - tempBlocksHeightSum);
				   r.pick();
				   mvRaise(r, upH - tempBlocksHeightSum);
				   mvContract(r, 8);
				   int lastTempH = temp.remove(temp.size() - 1);
				   lastBlockHeight += lastTempH;
				   tempBlocksHeightSum -= lastTempH;
				   mvLower(r, upH - lastBlockHeight);
				   r.drop();
				   mvRaise(r, upH - lastBlockHeight);
			   } else {
				   System.out.println("Error no required element in block array!");
			   }
		   }
	   } else {
		   // part A, B, C
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
   
//   private int findIndex(ArrayList<Integer> arr, int value) {
//	   int index = -1;
//	   for(int i = 0; i < arr.size(); i++) {
//		   if(arr.get(i) == value)
//			   index = i;
//	   }
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

