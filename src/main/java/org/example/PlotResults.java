package org.example;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;
import org.knowm.xchart.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlotResults {

    public static void plotTimes(List<Cloudlet> sequentialCloudlets, List<Cloudlet> greedyCloudlets) {
        List<Double> sequentialTimes = sequentialCloudlets.stream()
                .map(Cloudlet::getFinishTime)
                .collect(Collectors.toList());

        List<Double> greedyTimes = greedyCloudlets.stream()
                .map(Cloudlet::getFinishTime)
                .collect(Collectors.toList());

        if (sequentialTimes.isEmpty() || greedyTimes.isEmpty()) {
            System.err.println("Error: One of the times lists is empty.");
            return;
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).title("Cloudlet Finish Times")
                .xAxisTitle("Cloudlet").yAxisTitle("Finish Time (s)").build();

        chart.addSeries("Sequential", IntStream.range(0, sequentialTimes.size()).boxed().collect(Collectors.toList()), sequentialTimes);
        chart.addSeries("Greedy", IntStream.range(0, greedyTimes.size()).boxed().collect(Collectors.toList()), greedyTimes);

        new SwingWrapper<>(chart).displayChart();
    }

    public static void plotCosts(List<Cloudlet> sequentialCloudlets, List<Cloudlet> greedyCloudlets) {
        double sequentialCosts = calculateTotalCost(sequentialCloudlets);
        double greedyCosts = calculateTotalCost(greedyCloudlets);

        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Cloudlet Cost Comparison")
                .xAxisTitle("Approach").yAxisTitle("Total Cost ($)").build();

        chart.addSeries("Costs", List.of("Sequential", "Greedy"), List.of(sequentialCosts, greedyCosts));

        new SwingWrapper<>(chart).displayChart();
    }

    private static double calculateTotalCost(List<Cloudlet> cloudlets) {
        double totalCost = 0.0;
        double cpuCostPerSecond = 0.02 / 3600; // $0.02 per core-hour, converted to per second
        double ramCostPerGBPerSecond = 0.01 / 3600; // $0.01 per GB-hour, converted to per second
        double storageCostPerGB = 0.001; // $0.001 per GB
        double bandwidthCostPerGB = 0.005; // $0.005 per GB

        for (Cloudlet cloudlet : cloudlets) {
            Vm vm = cloudlet.getVm();
            if (vm == null) {
                continue; // If the cloudlet is not assigned to a VM, skip it.
            }
            totalCost += (cpuCostPerSecond * cloudlet.getVm().getPesNumber())
                    + (ramCostPerGBPerSecond * vm.getRam().getAllocatedResource())
                    + (storageCostPerGB * vm.getStorage().getAllocatedResource())
                    + (bandwidthCostPerGB * vm.getBw().getAllocatedResource());
        }

        return totalCost;
    }
}
