package by.hut.flat.calendar.internal;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class SamplePagerAdapter extends PagerAdapter{
    
    List<View> pages = null;
    
    public SamplePagerAdapter(List<View> pages){
        this.pages = pages;
    }
    
    @Override
    public Object instantiateItem(ViewGroup collection, int position){
        View v = pages.get(position);
        collection.addView(v, 0);
        return v;
    }
    
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view){
        collection.removeView((View) view);
    }
    @Override
    public int getCount(){
        return pages.size();
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object){
        return view.equals(object);
    }

    @Override
    public void finishUpdate(View arg0){
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1){
    }

    @Override
    public Parcelable saveState(){
        return null;
    }

    @Override
    public void startUpdate(View arg0){
    }
    public static int Int(String str){
		return Integer.parseInt(str);
	}
}
