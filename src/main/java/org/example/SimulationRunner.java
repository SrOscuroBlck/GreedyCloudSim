package org.example;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.vms.Vm;

import java.util.List;

public class SimulationRunner {

    public static void runSimulations() {
        // Sequential Approach: Single VM running all tasks sequentially
        CloudSimPlus simulation1 = new CloudSimPlus();
        DatacenterBrokerSimple broker1 = new DatacenterBrokerSimple(simulation1);
        CloudSimConfig.createDatacenter(simulation1);
        List<Vm> vmList1 = CloudSimConfig.createSinglePowerfulVM();
        List<Cloudlet> cloudletList1 = ComplexTaskGenerator.createComplexCloudlets(10);

        broker1.submitVmList(vmList1);
        double delay = 0;
        for (Cloudlet cloudlet : cloudletList1) {
            broker1.submitCloudletList(List.of(cloudlet), delay);
            delay += (int) ((cloudlet.getLength() / cloudlet.getVm().getMips()) + 0.1);
        }

        Vm singleVm = vmList1.get(0);
        SequentialScheduler sequentialScheduler = new SequentialScheduler(broker1, singleVm, cloudletList1);
        sequentialScheduler.schedule();

        simulation1.start();
        List<Cloudlet> finishedCloudlets1 = broker1.getCloudletFinishedList();

        // Greedy Approach: Multiple VMs running tasks using Greedy Algorithm
        CloudSimPlus simulation2 = new CloudSimPlus();
        DatacenterBrokerSimple broker2 = new DatacenterBrokerSimple(simulation2);
        CloudSimConfig.createDatacenter(simulation2);
        List<Vm> vmList2 = CloudSimConfig.createVmsWithDifferentCharacteristics();
        List<Cloudlet> cloudletList2 = ComplexTaskGenerator.createComplexCloudlets(10);

        broker2.submitVmList(vmList2);
        delay = 0;
        for (Cloudlet cloudlet : cloudletList2) {
            broker2.submitCloudletList(List.of(cloudlet), delay);
            delay += (int) ((cloudlet.getLength() / cloudlet.getVm().getMips()) + 0.1);
        }

        GreedyScheduler greedyScheduler = new GreedyScheduler(broker2, vmList2, cloudletList2);
        greedyScheduler.schedule();

        simulation2.start();
        List<Cloudlet> finishedCloudlets2 = broker2.getCloudletFinishedList();

        // Plot the results
        PlotResults.plotTimes(finishedCloudlets1, finishedCloudlets2);
        PlotResults.plotCosts(finishedCloudlets1, finishedCloudlets2);
    }
}
