package com.cloudeducate.redtick.Utils.decorators;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.cloudeducate.redtick.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by yogesh on 29/1/16.
 */
public class NewPresentDecor implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private Drawable drawable;

    public NewPresentDecor(int color, Collection<CalendarDay> dates, Activity activity) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        drawable = activity.getResources().getDrawable(R.drawable.present_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));
        view.setBackgroundDrawable(drawable);
    }
}

