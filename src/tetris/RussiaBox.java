package tetris;
/**
 * ����������,���Ʒ������ɫ
 * @author ������
 *
 */

public class RussiaBox implements Cloneable{
	
	 private boolean isColor;
	 
	 public RussiaBox(boolean isColor)
	 {
	   this.isColor = isColor;
	 }
	 /*
	  *������ɫ
	 */
	 public void setColor(boolean isColor)
	 {
	   this.isColor=isColor;
	 }
	 /*
	 *������ɫ
	 */
	 public boolean isColorBox()
	 {
	   return isColor;
	 }
	 /*
	  * @see Java.lang.Object#clone()
	  */
	 public Object clone()
	 {
	  Object o = null;
	  try
	  {
	   o=super.clone();
	  }catch(CloneNotSupportedException e)
	  {
	   e.printStackTrace();
	  }
	  return o;
	 }

}
