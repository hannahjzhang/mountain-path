import java.util.*;
import java.io.*;
import java.awt.*;

public class MapDataDrawer
{

  private int[][] grid;
  
  // step 1: reading data
  public MapDataDrawer(String filename, int rows, int cols){
      // initialize grid 
      //read the data from the file into the grid
      grid = new int[rows][cols];
      
      // searched up help
      // file not found exception, need to use try and catch
      try {
      // searched up for help
      Scanner in = new Scanner(new File (filename));
      
      // loads the number into the array
      for (int i = 0; i < grid.length; i++)
      {
    	  for (int j = 0; j < grid[0].length; j++)
    	  {
    		  if (in.hasNextInt())
    		  {
    			  grid[i][j] = in.nextInt();
    		  }
    		  else
    		  {
    			  System.err.println("Not enough numbers");
    			  System.exit(- 1);
    		  }
    	  }
      }
      } catch (FileNotFoundException e)
      {
    	  System.err.println("File not found:" + filename);
    	  System.exit(- 1);
      }
  }
  
  // step 2: min and max
  /**
   * @return the min value in the entire grid
   */
  public int findMinValue(){
	  int currentmin = 1000000;
	  for (int i = 0; i < grid.length; i++)
	  {
		  for (int j = 0; j < grid[0].length; j++)
		  {
			  if (grid[i][j] < currentmin)
			  {
				  currentmin = grid[i][j];
			  }
		  }
	  }
    return currentmin;    
  }
  /**
   * @return the max value in the entire grid
   */
  public int findMaxValue(){
	  int currentmax = 0;
	  for (int i = 0; i < grid.length; i++)
	  {
		  for (int j = 0; j < grid[0].length; j++)
		  {
			  if (grid[i][j] > currentmax)
			  {
				  currentmax = grid[i][j];
			  }
		  }
	  }
      return currentmax;
  }
  
  /**
   * @param col the column of the grid to check
   * @return the index of the row with the lowest value in the given col for the grid
   */
  public  int indexOfMinInCol(int col){
	  int min = 1000000;
	  int index = 0;
	  
	  for (int i = 0; i < grid.length; i++)
	  {
		  if (grid[i][col] < min)
		  {
			  min = grid[i][col];
			  index = i;
		  }
	  }
      return index;
  }
  
  // step 3: draw the map
  /**
   * Draws the grid using the given Graphics object.
   * Colors should be grayscale values 0-255, scaled based on min/max values in grid
   */
  public void drawMap(Graphics g){
	  int min = findMinValue();
	  int max = findMaxValue();
      for (int i = 0; i < grid.length; i++)
      {
    	  for (int j = 0; j < grid[0].length; j++)
    	  {
    	 	 // convert each number to a num from 0-256, then convert to a color
    	 	 // found the formula by practicing with smaller numbers
    		 // 255 (colorscale) * (num - min) / range
    		 int finalNum = 255 * (grid[i][j] - min) / (max - min + 1);
    		 
    		 // the instruction doc provides this information
    		 g.setColor(new Color(finalNum, finalNum, finalNum));
    		 g.fillRect(j, i, 1, 1);
    	  }
      }
  }

  // step 4: drawing the path
   /**
   * Find a path from West-to-East starting at given row.
   * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
   * @return the total change in elevation traveled from West-to-East
   */
  public int drawLowestElevPath(Graphics g, int row){
	  int totalChange = 0;
	  int currentRow = row;
	  
    for (int i = 0; i < grid[0].length - 1; i++)
    {
    	// set to a big number so it will never be called if currentRow  is less than zero
    	// in arrays, need to check the boundaries (as seen in up and down)
    	int up = 1000000;
    	if (currentRow > 0)
    	{
    		up = Math.abs(grid[currentRow][i] - grid[currentRow - 1][i + 1]);
    	}
    	int forward = Math.abs(grid[currentRow][i] - grid[currentRow][i + 1]);
    	int down = 1000000;
    	if (currentRow < grid.length - 1)
    	{
    		down = Math.abs(grid[currentRow][i] - grid[currentRow + 1][i + 1]);
    	}
    	
    	if ((forward <= up) && (forward <= down))
    	{
    		g.fillRect(i + 1, currentRow, 1, 1);
    		totalChange = totalChange + forward;
    	}
    	else if (up < down)
    	{
    		g.fillRect(i + 1, currentRow - 1, 1, 1);
    		totalChange = totalChange + up;
    		currentRow = currentRow - 1;
    	}
    	else if (down < up)
    	{
    		g.fillRect(i + 1, currentRow + 1, 1, 1);
    		totalChange = totalChange + down;
    		currentRow = currentRow + 1;
    	}
    	else
    	{
    		Random random = new Random();
    		int direction = random.nextInt(2);
    		if (direction == 0)
    		{
    			g.fillRect(i + 1, currentRow - 1, 1, 1);
    			totalChange = totalChange + up;
    			currentRow = currentRow - 1;
    		}
    		else
    		{
    			g.fillRect(i + 1, currentRow + 1, 1, 1);
    			totalChange = totalChange + down;
    			currentRow = currentRow + 1;
    		}
    	}
    }
    return totalChange;
  }
  
  /**
   * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
   */
  public int indexOfLowestElevPath(Graphics g){
	// remember, when finding min and max, must keep a variable to keep track of current min/max
	  int minSoFar = 1000000;
	  int index = 0;
     for (int i = 0; i < grid.length - 1; i++)
     {
    	 int totalChange = drawLowestElevPath(g, i);
    	 if (totalChange < minSoFar)
    	 {
    		 // don't need to set color because it is already done in driver
    		 index = i;
    		 minSoFar = totalChange;
    	 }
     }
     return index;
  
  }
  
  
}