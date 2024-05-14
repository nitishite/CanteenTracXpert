package com.example.christcanteenonlineorderingsystem;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Ensure the time is at least 15 minutes from now
        c.add(Calendar.MINUTE, 15);

        // Limit the selectable time between 9 AM and 4 PM
        int hourLimit = Math.max(9, c.get(Calendar.HOUR_OF_DAY));
        int minuteLimit = c.get(Calendar.MINUTE);

        TextView textView = new TextView(getActivity());
        textView.setText("Select a time slot:");
        textView.setTextSize(25);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hourLimit, minuteLimit, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.setCustomTitle(textView);
        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Handle the time picked event
        listener.onTimePicked(hourOfDay, minute);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TimePickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TimePickedListener");
        }
    }

    public interface TimePickedListener {
        void onTimePicked(int hourOfDay, int minute);
    }
}
