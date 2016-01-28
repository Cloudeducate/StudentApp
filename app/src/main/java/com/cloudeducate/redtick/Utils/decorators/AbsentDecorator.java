package com.cloudeducate.redtick.Utils.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.cloudeducate.redtick.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Created by yogesh on 28/1/16.
 */
public class AbsentDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private final Calendar calendar = Calendar.getInstance();


    public AbsentDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.absent_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
