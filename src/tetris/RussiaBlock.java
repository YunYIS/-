package tetris;
/**
 * 方块类
 * @author 张云天
 *
 */

public class RussiaBlock extends Thread{
	private int style,y,x,level;
	private boolean moving,pausing;
	private RussiaBox boxes[][];
	private GameCanvas canvas;
	
	//boxes[4][4]
	public final static int ROWS = 4; 
	public final static int COLS = 4;
	
	public final static int BLOCK_KIND_NUMBER = 7; //形状数
	public final static int BLOCK_STATUS_NUMBER = 4; //方块状态数
	public final static int BETWEEN_LEVELS_TIME = 50;
	public final static int LEVEL_FLATNESS_GENE = 3;
	/*
	  *方块的所有风格及其不同的状态
	  */
	public final static int[][] STYLES = {// 共28种状态
	{0x0f00, 0x4444, 0x0f00, 0x4444}, // 长条型的四种状态
	{0x04e0, 0x0464, 0x00e4, 0x04c4}, // 'T'型的四种状态
	  {0x4620, 0x6c00, 0x4620, 0x6c00}, // 反'Z'型的四种状态
	  {0x2640, 0xc600, 0x2640, 0xc600}, // 'Z'型的四种状态
	  {0x6220, 0x1700, 0x2230, 0x0740}, // '7'型的四种状态
	  {0x6440, 0x0e20, 0x44c0, 0x8e00}, // 反'7'型的四种状态
	  {0x0660, 0x0660, 0x0660, 0x0660}, // 方块的四种状态
	 }; 
	 /*
	  *构造函数
	  */
	 public RussiaBlock(int y,int x,int level,int style)
	 {
	   
	   this.y = y;
	   this.x = x;
	   this.level = level;
	   moving = true;
	   pausing = false;
	   this.style = style;
	   
	   canvas = GameCanvas.getCanvasInstance();
	   
	   boxes = new RussiaBox[ROWS][COLS];
	   int key = 0x8000;
	   for(int i = 0; i < boxes.length; i++)
	     for(int j = 0; j < boxes[i].length; j++)
	     {
	        boolean isColor = ( (style & key) != 0 );
	       boxes[i][j] = new RussiaBox(isColor);
	       key = key >> 1;
	     }
	    display();
	 }
	 /*
	  *线程的 run方法控制放块的 下落 及下落速度
	  */
     @Override
	 public void run()
	 {
		  while(moving)
		  {
		   try
		   {
			    sleep( BETWEEN_LEVELS_TIME * (RussiaBlocksGame.MAX_LEVEL - level + LEVEL_FLATNESS_GENE) );
			     if(!pausing)
			         moving = ( moveTo(y + 1,x) &&  moving );
		   }catch(InterruptedException e)
		   {
			   e.printStackTrace();
		   }
		  }
	 }
	 /*
	  *暂停移动
	  */
	 public void pauseMove()
	 {
	   pausing =  true;
	 }
	 /*
	  *从暂停状态恢复
	  */
	 public void resumeMove()
	 {
	  pausing = false;
	 }
	 
	 /*
	  *停止移动
	  */
	 public void stopMove()
	 {
	   moving = false;
	 }
	/*
	 *向左移一格
	 */
	 public void moveLeft()
	 {
	   moveTo(y , x - 1);
	 }
	 /*
	 *向右移一格
	 */
	 public void moveRight()
	 {
	   moveTo(y , x + 1);
	 }
	 /*
	 *向下移一格,返回与其他几个不同，为了一键到底
	 */
	 public boolean moveDown()
	 {
	  if(moveTo(y + 1, x)) 
	       return true;
	  else
	       return false;
	 }
	 /*
	  *移到newRow,newCol位置
	  */
	 public synchronized boolean moveTo(int newRow, int newCol)
	 {
	    //erase();//必须在判断前进行擦除，否则isMoveable将产生错误行为
	    
	    if(!moving || !isMoveable(newRow,newCol))
	    {
	       display();
	        return false;
	    }
	    y = newRow;
	    x = newCol;
	    display();
	    canvas.repaint();
	    return true;
	 }
	 /*
	  *判断能否移到newRow,newCol位置
	  */
	 private boolean isMoveable(int newRow, int newCol)
	 {
	     erase();
	     for(int i = 0; i < boxes.length; i ++)
	      for(int j = 0; j< boxes[i].length; j ++ )
	        {
	          if( boxes[i][j].isColorBox() )
	          {
	            RussiaBox box = canvas.getBox(newRow + i, newCol + j);
	            if(box == null || box.isColorBox())
	                return false;
	          }          
	        }
	       return true;
	 }
	 /*
	  *通过旋转变为下一种状态
	  */
	  public void turnNext()
	 {
	   int newStyle = 0;
	   for(int i = 0; i < STYLES.length; i ++)
	    for(int j = 0 ;j < STYLES[i].length; j++)
	      {
	       if(style == STYLES[i][j])
	       {
	            newStyle = STYLES[i][(j + 1) % BLOCK_STATUS_NUMBER];
	            break;
	        }
	      }
	      turnTo(newStyle);
	 }
	 /*
	  * 通过旋转变能否变为newStyle状态
	  * (通过synchronized锁定)
	  */
	 private synchronized boolean turnTo(int newStyle)
	 {
	   //erase();//擦除之后在判断isTurnNextAble
	   if(!moving || !isTurnable(newStyle)) 
	   {     
	         display();
	         return false;
	   }
	   
	   style=newStyle;
	   int key = 0x8000;
	  
	   /*
	    * 确定在4x4矩阵中方块的形状
	    */
	   for(int i = 0; i < boxes.length; i ++)
	    for(int j = 0 ;j < boxes[i].length; j++)
	      {
	       boolean isColor = ((key & style) != 0 );
	       boxes[i][j].setColor(isColor);
	       key >>= 1; //右移赋值
	      }
	      /*
	       * (x+i,y+j)方块所占方格在画布中的坐标
	       * getBox()获取方块实例
	       */
	      display();
	      //重绘画布，用于显示方块
	      canvas.repaint();
	      return true;
	 }
	 /*
	  *判断通过旋转能否变为下一种状态
	  */
	 private boolean isTurnable(int newStyle)
	 {
	   erase();
	   int key = 0x8000; 
	   for(int i = 0; i< boxes.length; i++)
	      for(int j=0; j<boxes[i].length; j++)
	        {
	         if((key & newStyle) != 0)
	          {
	            RussiaBox box = canvas.getBox(y + i, x + j);
	            /*
	             * 超出画布或与已有方块重叠不能变换
	             */
	            if(box == null || box.isColorBox())
	                return false;
	          }
	          key >>= 1;
	        }
	    return true;
	 }
	 /*
	  *擦除当前方块(只是设置isColor属性,颜色并没有清除,为了判断能否移动之用)
	  */
	 private void erase()
	 { 
	   for(int i = 0; i < boxes.length; i ++)
	      for(int j = 0; j< boxes[i].length; j ++ )
	        {
	          if( boxes[i][j].isColorBox() )
	          {
	             RussiaBox box = canvas.getBox( y + i, x + j);
	             if(box != null)
	             box.setColor(false);
	           }
	         }
	 }
	 /*
	 *显示当前方块(其实只是设置Color属性,在调用repaint方法时才真正显示)
	 */
	 private void display()
	 {
	   for(int i = 0; i < boxes.length; i ++)
	      for(int j = 0;j< boxes[i].length ; j ++)
	      {
	         if(boxes[i][j].isColorBox())
	         {
	          RussiaBox box = canvas.getBox( y + i, x + j);
	          if(box != null)
	           box.setColor( true );
	         }
	      }
	 }
}
