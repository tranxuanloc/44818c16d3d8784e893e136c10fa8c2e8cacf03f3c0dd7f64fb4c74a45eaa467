package com.scsvn.whc_2016.main.booking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

/**
 * Created by xuanloc on 12/14/2016.
 */
public class BarGraph {
    private List<Booking> bookings;
    private Context context;
    private float yAxisMax;
    private int size;

    public BarGraph(Context context, List<Booking> bookings, float yAxisMax) {
        this.bookings = bookings;
        this.context = context;
        this.yAxisMax = yAxisMax;
        size = bookings.size();
    }

    public void show() {
        XYMultipleSeriesRenderer barChartRenderer = getBarChartRenderer();
        Intent intent = ChartFactory.getBarChartIntent(context, getBarDemoDataSet(), barChartRenderer, BarChart.Type.DEFAULT);
        context.startActivity(intent);
    }

    private XYMultipleSeriesDataset getBarDemoDataSet() {
        XYMultipleSeriesDataset barChartDataSet = new XYMultipleSeriesDataset();
        XYSeries weightAll = new XYSeries("All");
        XYSeries weightRO = new XYSeries("RO");
        XYSeries weightDO = new XYSeries("DO");
        for (int i = 0; i < size; i++) {
            Booking booking = bookings.get(i);
            weightAll.add(i, booking.getWeightAll());
            weightRO.add(i, booking.getWeightRO());
            weightDO.add(i, booking.getWeightDO());
        }
        barChartDataSet.addSeries(weightAll);
        barChartDataSet.addSeries(weightRO);
        barChartDataSet.addSeries(weightDO);

        return barChartDataSet;
    }

    public XYMultipleSeriesRenderer getBarChartRenderer() {
        XYSeriesRenderer allRenderer = new XYSeriesRenderer();
        allRenderer.setColor(Color.RED);
        allRenderer.setFillPoints(true);
        allRenderer.setLineWidth(1);
        allRenderer.setDisplayChartValues(false);

        XYSeriesRenderer roRenderer = new XYSeriesRenderer();
        roRenderer.setColor(Color.BLUE);
        roRenderer.setFillPoints(true);
        roRenderer.setLineWidth(1);
        roRenderer.setDisplayChartValues(false);

        XYSeriesRenderer doRenderer = new XYSeriesRenderer();
        doRenderer.setColor(Color.rgb(0x33, 0x69, 0x1E));
        doRenderer.setFillPoints(true);
        doRenderer.setLineWidth(1);
        doRenderer.setDisplayChartValues(false);

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//        renderer.setXTitle("Time slot");
//        renderer.setYTitle("Weight");
        renderer.setXLabels(0);
        renderer.setLabelsTextSize(18);
        renderer.setLegendTextSize(18);
        renderer.setZoomButtonsVisible(false);
        renderer.setPanEnabled(false, false);
        renderer.setShowGrid(true);
        renderer.setFitLegend(true);
        renderer.setZoomEnabled(false);
        renderer.setExternalZoomEnabled(false);
        renderer.setAntialiasing(true);
        renderer.setInScroll(false);
        renderer.setLegendHeight(30);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setXLabelsAlign(Paint.Align.CENTER);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setYLabels(8);
        renderer.setYAxisMax(yAxisMax);
        renderer.setXAxisMin(-0.5);
        renderer.setXAxisMax(size);
        renderer.setBarSpacing(0.2);
        renderer.setBackgroundColor(Color.WHITE);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setApplyBackgroundColor(true);
        renderer.setMargins(new int[]{0, 35, 10, 10});
        for (int i = 0; i < size; i++) {
            Booking item = bookings.get(i);
            renderer.addXTextLabel(i, item.getTimeSlotId() + "");
        }
        renderer.addSeriesRenderer(allRenderer);
        renderer.addSeriesRenderer(roRenderer);
        renderer.addSeriesRenderer(doRenderer);
        return renderer;
    }

}
