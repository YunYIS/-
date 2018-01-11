package tetris;
/**
 * 单个方格类,控制方格的颜色
 * @author 张云天
 *
 */

public class RussiaBox implements Cloneable{
	
	 private boolean isColor;
	 
	 public RussiaBox(boolean isColor)
	 {
	   this.isColor = isColor;
	 }
	 /*
	  *设置颜色
	 */
	 public void setColor(boolean isColor)
	 {
	   this.isColor=isColor;
	 }
	 /*
	 *返回颜色
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
