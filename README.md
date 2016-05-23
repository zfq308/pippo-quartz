Pippo Quartz
=====================
[Quartz](http://www.quartz-scheduler.org/) integration for [Pippo](http://www.pippo.ro/)

Sample code
---------------
First we must add in `src/main/resources/conf/application.properties`:

```
scheduler.quartz.enabled = true
org.quartz.scheduler.instanceName = MyScheduler
org.quartz.threadPool.threadCount = 3
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore

org.quartz.plugin.jobInitializer.class =org.quartz.plugins.xml.XMLSchedulingDataProcessorPlugin
org.quartz.plugin.jobInitializer.fileNames = conf/quartz-config.xml
org.quartz.plugin.jobInitializer.failOnFileNotFound = true
```

Then, we must create the Quartz configuration file: `src/main/resources/conf/quartz-config.xml`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<job-scheduling-data
    xmlns="http://www.quartz-scheduler.org/xml/JobSchedulingData"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.quartz-scheduler.org/xml/JobSchedulingData http://www.quartz-scheduler.org/xml/job_scheduling_data_1_8.xsd"
    version="1.8">
    
    <pre-processing-commands>
        <delete-jobs-in-group>*</delete-jobs-in-group>  <!-- clear all jobs in scheduler -->
        <delete-triggers-in-group>*</delete-triggers-in-group> <!-- clear all triggers in scheduler -->
    </pre-processing-commands>

    <processing-directives>
        <overwrite-existing-data>true</overwrite-existing-data>
        <ignore-duplicates>false</ignore-duplicates>
    </processing-directives>
    
    <schedule>
        
        <job>
            <name>JobA</name>
            <group>DummyGroup</group>
            <description>This is JobA</description>
            <job-class>ro.pippo.test.quartz.jobs.JobA</job-class>
        </job>
        
        <job>
            <name>JobB</name>
            <group>DummyGroup</group>
            <description>This is JobB</description>
            <job-class>ro.pippo.test.quartz.jobs.JobB</job-class>
        </job>
        
        <trigger>
            <cron>
                <name>TriggerA</name>
                <group>DummyTriggerGroup</group>
                <job-name>JobA</job-name>
                <job-group>DummyGroup</job-group>
                <description>This is TriggerA</description>
                <!-- It will run every 2 seconds -->
                <cron-expression>0/2 * * * * ?</cron-expression>
            </cron>
        </trigger>
        
        <trigger>
            <simple>
                <name>TriggerB</name>
                <group>DummyTriggerGroup</group>
                <job-name>JobB</job-name>
                <job-group>DummyGroup</job-group>
                <description>This is TriggerB</description>
                <repeat-count>5</repeat-count> <!-- repeat 5 times -->
                <repeat-interval>10000</repeat-interval>  <!--  every 10 seconds -->
            </simple>
        </trigger>
        
    </schedule>
</job-scheduling-data>
```

Then, we must create the jobs:

**JobA**
```java
package ro.pippo.test.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobA implements Job {
    
    private static final Logger LOG = LoggerFactory.getLogger(JobA.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Job A");
    }

}
```

**JobB**
```java
package ro.pippo.test.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobB implements Job {
    
    private static final Logger LOG = LoggerFactory.getLogger(JobB.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Job B");
    }

}
```
