package org.example;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

import java.util.Comparator;
import java.util.List;

public class GreedyScheduler {

    private final DatacenterBrokerSimple broker;
    private final List<Vm> vmList;
    private final List<Cloudlet> cloudletList;

    public GreedyScheduler(DatacenterBrokerSimple broker, List<Vm> vmList, List<Cloudlet> cloudletList) {
        this.broker = broker;
        this.vmList = vmList;
        this.cloudletList = cloudletList;
    }

    public void schedule() {
        for (Cloudlet cloudlet : cloudletList) {
            Vm bestVm = vmList.stream()
                    .min(Comparator.comparingLong(vm -> vm.getRam().getAllocatedResource()))
                    .orElse(null);

            if (bestVm != null) {
                cloudlet.setVm(bestVm);
                broker.submitCloudlet(cloudlet);
            }
        }
    }
}
