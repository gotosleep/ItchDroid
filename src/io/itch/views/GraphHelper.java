package io.itch.views;

import io.itch.models.GraphData;
import io.itch.models.GraphPoint;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;

public class GraphHelper {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat LABEL_DATE_FORMAT = new SimpleDateFormat("M/dd", Locale.US);
    public static final NumberFormat COUNT_FORMAT = NumberFormat.getInstance(Locale.getDefault());

    public static String[] getLabels(int days) {
        String result[] = new String[days];
        Calendar day = startDate(days);
        for (int i = 0; i < days; ++i) {
            result[i] = LABEL_DATE_FORMAT.format(day.getTime());
            day.add(Calendar.DATE, 1);
        }
        return result;
    }

    public static CustomLabelFormatter getLabelFormatter(int days) {
        final String[] labels = getLabels(days);
        return new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX && value < labels.length) {
                    return labels[(int) value];
                } else if (!isValueX) {
                    return COUNT_FORMAT.format(value);
                }
                return null; // let graphview generate Y-axis label for us
            }
        };
    }

    public static Calendar startDate(int days) {
        Calendar result = Calendar.getInstance();
        result.add(Calendar.DATE, -(days - 1));
        return result;
    }

    public static GraphViewSeries generateSeries(Context context, String label, List<GraphPoint> points, int days,
            int color) {
        GraphViewSeriesStyle style = new GraphViewSeriesStyle(color, ViewHelper.dpToPixels(context, 2));
        GraphData data = new GraphData(label, style, days, points);
        return data.getSeries();
    }

    public static void configureYAxis(GraphView graphView, int maxValue, int numSteps) {
        // why the - 1? we need to account for 0 being a step
        int step = (int) Math.ceil(maxValue / (float) (numSteps - 1));
        if (step <= 0) {
            step = 1;
        }
        graphView.getGraphViewStyle().setNumVerticalLabels(numSteps);
        graphView.setManualYAxisBounds(step * (numSteps - 1), 0);
    }
}
