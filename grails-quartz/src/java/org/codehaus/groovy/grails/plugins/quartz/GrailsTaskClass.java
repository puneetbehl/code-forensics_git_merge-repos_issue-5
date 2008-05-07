/* Copyright 2004-2005 the original author or authors.
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
package org.codehaus.groovy.grails.plugins.quartz;

import org.codehaus.groovy.grails.commons.InjectableGrailsClass;

/**
 * <p>Represents a task class in Grails</p> 
 * 
 * @author Micha?? K??ujszo
 * @author Graeme Rocher
 * @author Marcel Overdijk
 * @author Sergey Nebolsin
 * 
 * @since 0.2
 * 20-Apr-2006
 */
public interface GrailsTaskClass extends InjectableGrailsClass {
	
	/**
	 * Method which is executed by the task scheduler.
	 */
	public void execute();

	/**
	 * Get task timeout between executions.
	 */
	public long getTimeout();
	
	/**
	 * Get start delay before first execution after starting scheduler.
	 */
	public long getStartDelay();

    /**
     * Get the number of times the job should repeat, after which it will be automatically deleted
     */
    public int getRepeatCount();

    /**
	 * Get cron expression used for configuring scheduler.
	 */
	public String getCronExpression();
	
	/**
	 * Get group name used for configuring scheduler.
	 */
	public String getGroup();
	
	/**
	 * If cronExpression property is set returns true.
	 */
	public boolean isCronExpressionConfigured();
	
	/**
	 * If jobs can be executed concurrently returns true.
	 */
	public boolean isConcurrent();

	/**
	 * If task requires Hibernate Session bounded to thread returns true.
	 */
	public boolean isSessionRequired();
}
