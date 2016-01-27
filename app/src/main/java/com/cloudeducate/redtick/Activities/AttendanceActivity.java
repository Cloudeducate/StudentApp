package com.cloudeducate.redtick.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.decorators.AbsentDecorator;
import com.cloudeducate.redtick.Utils.decorators.PresentDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class AttendanceActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    MaterialCalendarView widget;
    TextView textView_date;

    private AbsentDecorator absentDecorator;
    private PresentDecorator presentDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presentDecorator = new PresentDecorator(AttendanceActivity.this);
        absentDecorator = new AbsentDecorator(AttendanceActivity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        textView_date = (TextView) findViewById(R.id.textView_date);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.addDecorators(
                new AbsentDecorator(this),
                new PresentDecorator(this),
                absentDecorator, presentDecorator
        );




    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        textView_date.setText(getSelectedDatesString());
        //If you change a decorate, you need to invalidate decorators
        presentDecorator.shouldDecorate(date);
        widget.invalidateDecorators();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

}
