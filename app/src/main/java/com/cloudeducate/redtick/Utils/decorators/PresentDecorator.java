package com.cloudeducate.redtick.Utils.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.cloudeducate.redtick.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Created by yogesh on 27/1/16.
 */
public class PresentDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private final Calendar calendar = Calendar.getInstance();

    public PresentDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.present_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        Log.v("MyTag","called");
        day.copyTo(calendar);
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}
