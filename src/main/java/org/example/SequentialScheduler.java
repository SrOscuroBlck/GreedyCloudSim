package org.example;

import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

import java.util.List;

public class SequentialScheduler {

    private final DatacenterBrokerSimple broker;
    private final Vm vm;
    private final List<Cloudlet> cloudletList;

    public SequentialScheduler(DatacenterBrokerSimple broker, Vm vm, List<Cloudlet> cloudletList) {
        this.broker = broker;
        this.vm = vm;
        this.cloudletList = cloudletList;
    }

    public void schedule() {
        for (Cloudlet cloudlet : cloudletList) {
            cloudlet.setVm(vm);
            broker.submitCloudlet(cloudlet);
        }
    }
}
