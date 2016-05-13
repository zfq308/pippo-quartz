/*
 * Copyright 2016 Herman Barrantes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.pippo.quartz;

import java.util.List;
import java.util.Properties;
import org.kohsuke.MetaInfServices;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.Initializer;

/**
 *
 * @author Herman Barrantes
 * @since 09/05/2016
 */
@MetaInfServices(Initializer.class)
public class QuartzInitializer implements Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzInitializer.class);

    private static final String SCHEDULER_ENABLED = "scheduler.quartz.enabled";
    private static final String QUARTZ_KEYS = "org.quartz.";
    private Scheduler scheduler;

    @Override
    public void init(Application application) {
        // Run scheduler only when activated in application.properties:
        Boolean enabled = application.getPippoSettings().getBoolean(SCHEDULER_ENABLED, false);
        if (enabled) {
            // Get the configuration from application.properties
            List<String> quartzKeys = application.getPippoSettings().getSettingNames(QUARTZ_KEYS);
            Properties properties = new Properties();
            quartzKeys.stream().forEach((key) -> {
                properties.put(key, application.getPippoSettings().getString(key, null));
            });
            // Run scheduler
            try {
                SchedulerFactory factory = new StdSchedulerFactory(properties);
                this.scheduler = factory.getScheduler();
                this.scheduler.start();
                LOG.debug("QuartzInitializer init");
            } catch (SchedulerException ex) {
                LOG.error("Error on init Quartz", ex);
            }
        }
    }

    @Override
    public void destroy(Application application) {
        try {
            if (this.scheduler != null) {
                this.scheduler.shutdown();
            }
            LOG.debug("QuartzInitializer destroy");
        } catch (SchedulerException ex) {
            LOG.error("Error on destroy Quartz", ex);
        }
    }

}
