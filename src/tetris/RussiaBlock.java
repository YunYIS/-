package tetris;
/**
 * ������
 * @author ������
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
	
	public final static int BLOCK_KIND_NUMBER = 7; //��״��
	public final static int BLOCK_STATUS_NUMBER = 4; //����״̬��
	public final static int BETWEEN_LEVELS_TIME = 50;
	public final static int LEVEL_FLATNESS_GENE = 3;
	/*
	  *��������з���䲻ͬ��״̬
	  */
	public final static int[][] STYLES = {// ��28��״̬
	{0x0f00, 0x4444, 0x0f00, 0x4444}, // �����͵�����״̬
	{0x04e0, 0x0464, 0x00e4, 0x04c4}, // 'T'�͵�����״̬
	  {0x4620, 0x6c00, 0x4620, 0x6c00}, // ��'Z'�͵�����״̬
	  {0x2640, 0xc600, 0x2640, 0xc600}, // 'Z'�͵�����״̬
	  {0x6220, 0x1700, 0x2230, 0x0740}, // '7'�͵�����״̬
	  {0x6440, 0x0e20, 0x44c0, 0x8e00}, // ��'7'�͵�����״̬
	  {0x0660, 0x0660, 0x0660, 0x0660}, // ���������״̬
	 }; 
	 /*
	  *���캯��
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
	  *�̵߳� run�������Ʒſ�� ���� �������ٶ�
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
	  *��ͣ�ƶ�
	  */
	 public void pauseMove()
	 {
	   pausing =  true;
	 }
	 /*
	  *����ͣ״̬�ָ�
	  */
	 public void resumeMove()
	 {
	  pausing = false;
	 }
	 
	 /*
	  *ֹͣ�ƶ�
	  */
	 public void stopMove()
	 {
	   moving = false;
	 }
	/*
	 *������һ��
	 */
	 public void moveLeft()
	 {
	   moveTo(y , x - 1);
	 }
	 /*
	 *������һ��
	 */
	 public void moveRight()
	 {
	   moveTo(y , x + 1);
	 }
	 /*
	 *������һ��,����������������ͬ��Ϊ��һ������
	 */
	 public boolean moveDown()
	 {
	  if(moveTo(y + 1, x)) 
	       return true;
	  else
	       return false;
	 }
	 /*
	  *�Ƶ�newRow,newColλ��
	  */
	 public synchronized boolean moveTo(int newRow, int newCol)
	 {
	    //erase();//�������ж�ǰ���в���������isMoveable������������Ϊ
	    
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
	  *�ж��ܷ��Ƶ�newRow,newColλ��
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
	  *ͨ����ת��Ϊ��һ��״̬
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
	  * ͨ����ת���ܷ��ΪnewStyle״̬
	  * (ͨ��synchronized����)
	  */
	 private synchronized boolean turnTo(int newStyle)
	 {
	   //erase();//����֮�����ж�isTurnNextAble
	   if(!moving || !isTurnable(newStyle)) 
	   {     
	         display();
	         return false;
	   }
	   
	   style=newStyle;
	   int key = 0x8000;
	  
	   /*
	    * ȷ����4x4�����з������״
	    */
	   for(int i = 0; i < boxes.length; i ++)
	    for(int j = 0 ;j < boxes[i].length; j++)
	      {
	       boolean isColor = ((key & style) != 0 );
	       boxes[i][j].setColor(isColor);
	       key >>= 1; //���Ƹ�ֵ
	      }
	      /*
	       * (x+i,y+j)������ռ�����ڻ����е�����
	       * getBox()��ȡ����ʵ��
	       */
	      display();
	      //�ػ滭����������ʾ����
	      canvas.repaint();
	      return true;
	 }
	 /*
	  *�ж�ͨ����ת�ܷ��Ϊ��һ��״̬
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
	             * ���������������з����ص����ܱ任
	             */
	            if(box == null || box.isColorBox())
	                return false;
	          }
	          key >>= 1;
	        }
	    return true;
	 }
	 /*
	  *������ǰ����(ֻ������isColor����,��ɫ��û�����,Ϊ���ж��ܷ��ƶ�֮��)
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
	 *��ʾ��ǰ����(��ʵֻ������Color����,�ڵ���repaint����ʱ��������ʾ)
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
